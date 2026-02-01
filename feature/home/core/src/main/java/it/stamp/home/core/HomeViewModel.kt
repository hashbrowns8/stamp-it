package it.stamp.home.core

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.exception.MissionException
import it.stamp.domain.usecase.group.ObserveMyGroupUseCase
import it.stamp.domain.usecase.membership.ObserveMyGroupMembers
import it.stamp.domain.usecase.mission.CompleteMission
import it.stamp.domain.usecase.mission.ObserveMissionsAssignedByUser
import it.stamp.domain.usecase.mission.ObserveMissionsAssignedToMeForThisWeek
import it.stamp.domain.usecase.mission.UndoMissionCompletion
import it.stamp.domain.usecase.stamp.ObserveGroupStampCountsForMonthUseCase
import it.stamp.domain.usecase.user.ObserveCurrentUserUseCase
import it.stamp.home.core.ui.LeaderboardEntryUiModel
import it.stamp.home.core.ui.MemberUiModel
import it.stamp.home.core.ui.MyMissionUiModel
import it.stamp.model.ids.MissionId
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.mission.Mission
import it.stamp.model.user.User
import it.stamp.ui.MemberMissionUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeMyGroupUseCase: ObserveMyGroupUseCase,
    observeMyGroupMembers: ObserveMyGroupMembers,
    observeGroupStampCountsForMonthUseCase: ObserveGroupStampCountsForMonthUseCase,
    observeMissionsAssignedToMeForThisWeek: ObserveMissionsAssignedToMeForThisWeek,
    observeMissionsAssignedByUser: ObserveMissionsAssignedByUser,
    private val completeMissionUseCase: CompleteMission,
    private val undoMissionCompletionUseCase: UndoMissionCompletion,
    private val timeZone: TimeZone,
) : ViewModel() {

    private val user: StateFlow<User?> = observeCurrentUserUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val members: StateFlow<List<Member>> = observeMyGroupMembers()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val rankings = user.filterNotNull()
        .flatMapLatest { user ->
            members.filterNot(List<Member>::isEmpty)
                .combine(observeGroupStampCountsForMonthUseCase()) { members, stampCountByMemberId ->
                    members.map { member ->
                        with(member) {
                            LeaderboardEntryUiModel(
                                member = MemberUiModel(
                                    id,
                                    avatar,
                                    displayName.value,
                                ),
                                isMe = id == user.id,
                                rank = 0,
                                stampCount = stampCountByMemberId.getOrDefault(id, 0),
                            )
                        }
                    }.sortedByDescending { entry ->
                        entry.stampCount
                    }.mapIndexed { index, entry ->
                        entry.copy(rank = index + 1)
                    }
                }
                .flowOn(Dispatchers.Default)
        }

    private val myMissions = members.filterNot(List<Member>::isEmpty)
        .combine(observeMissionsAssignedToMeForThisWeek()) { members, missions ->
            missions.filterNot(Mission::isDone)
                .map { mission ->
                    with(mission) {
                        val assigner = members.first { it.id == assigner }

                        val dueDate = dueDate
                            .toLocalDateTime(timeZone)
                            .date

                        MyMissionUiModel(
                            id,
                            category,
                            title,
                            dueDate,
                            assignerName = assigner.displayName.value,
                        )
                    }
                }
        }

    private val memberMissions = members.filterNot(List<Member>::isEmpty)
        .combine(observeMissionsAssignedByUser()) { members, missions ->
            missions.map { mission ->
                with(mission) {
                    val daysAgo = when (val daysUntilDue = daysUntilDue()) {
                        0 -> "오늘"
                        1 -> "내일"
                        else -> "${daysUntilDue}일 전"
                    }

                    val assignee = members.first { it.id == assignee }

                    val dueDate = dueDate
                        .toLocalDateTime(timeZone)
                        .date

                    MemberMissionUiModel(
                        id,
                        category,
                        title,
                        assigneeId = assignee.id,
                        assigneeDisplayName = assignee.displayName.value,
                        dueDate,
                        daysAgo,
                        status,
                        isOverdue(),
                        isDone,
                    )
                }
            }
        }

    val uiState: StateFlow<HomeUiState> = user.filterNotNull().flatMapLatest { user ->
        combine(
            observeMyGroupUseCase().filterNotNull(),
            members,
            rankings,
            myMissions,
            memberMissions,
        ) { group, members, rankings, myMissions, memberMissions ->
            HomeUiState.Success(
                user,
                group,
                members,
                rankings,
                myMissions,
                memberMissions,
            ) as HomeUiState
        }.catch { throwable ->
            emit(HomeUiState.Failure(throwable))
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState.Loading)

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent: SharedFlow<HomeUiEvent> = _uiEvent.asSharedFlow()

    fun completeMission(missionId: MissionId) {
        viewModelScope.launch {
            completeMissionUseCase(missionId)
                .onSuccess { mission ->
                    _uiEvent.emit(HomeUiEvent.CompleteMission(mission))
                }
                .onFailure { throwable ->
                    if (throwable is MissionException) {
                        _uiEvent.emit(HomeUiEvent.UpdateMissionStatusFailed(throwable))
                    } else {
                        handleFailure(throwable)
                    }
                }
        }
    }

    fun cancelMissionCompletion(missionId: MissionId) {
        viewModelScope.launch {
            undoMissionCompletionUseCase(missionId)
                .onSuccess { mission ->
                    _uiEvent.emit(HomeUiEvent.UndoMissionCompletion(mission))
                }
                .onFailure { throwable ->
                    if (throwable is MissionException) {
                        _uiEvent.emit(HomeUiEvent.UpdateMissionStatusFailed(throwable))
                    } else {
                        handleFailure(throwable)
                    }
                }
        }
    }

    private fun handleFailure(throwable: Throwable) {
        viewModelScope.launch {
            _uiEvent.emit(HomeUiEvent.OperationFailed(throwable))
        }
    }
}

@Stable
sealed interface HomeUiState {
    @Immutable
    data object Loading : HomeUiState

    @Immutable
    data class Success(
        val user: User,
        val group: Group,
        val members: List<Member>,
        val rankings: List<LeaderboardEntryUiModel>,
        val myMissions: List<MyMissionUiModel>,
        val memberMissions: List<MemberMissionUiModel>,
    ) : HomeUiState

    @Immutable
    data class Failure(val throwable: Throwable) : HomeUiState
}