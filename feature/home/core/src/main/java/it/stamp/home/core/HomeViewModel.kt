@file:Suppress("UNCHECKED_CAST")

package it.stamp.home.core

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.exception.MissionException
import it.stamp.domain.usecase.group.GetMyGroupLeaderboardUseCase
import it.stamp.domain.usecase.group.GetMyGroupUseCase
import it.stamp.domain.usecase.membership.GetMyGroupMembersUseCase
import it.stamp.domain.usecase.membership.ObserveMyMembershipUseCase
import it.stamp.domain.usecase.mission.CancelMissionCompletionUseCase
import it.stamp.domain.usecase.mission.CompleteMissionUseCase
import it.stamp.domain.usecase.mission.GetMyGroupMembersMissionsUseCase
import it.stamp.domain.usecase.mission.ObserveMyMissionsThisWeekUseCase
import it.stamp.domain.usecase.user.ObserveCurrentUserUseCase
import it.stamp.model.ids.MissionId
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.mission.Mission
import it.stamp.model.stamp.LeaderboardMember
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeMyMembershipUseCase: ObserveMyMembershipUseCase,
    private val getMyGroupUseCase: GetMyGroupUseCase,
    private val getMyGroupMembersUseCase: GetMyGroupMembersUseCase,
    private val getMyGroupLeaderboardUseCase: GetMyGroupLeaderboardUseCase,
    private val observeMyMissionsThisWeekUseCase: ObserveMyMissionsThisWeekUseCase,
    private val getMyGroupMembersMissionsUseCase: GetMyGroupMembersMissionsUseCase,
    private val completeMissionUseCase: CompleteMissionUseCase,
    private val cancelMissionCompletionUseCase: CancelMissionCompletionUseCase,
) : ViewModel() {

    private val retry = MutableStateFlow(0)

    fun retry() {
        retry.value += 1
    }

    private val membership = observeMyMembershipUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // TODO
    val uiState: StateFlow<HomeUiState> = membership.filterNotNull()
        .distinctUntilChangedBy { it.id }
        .combineTransform(retry) { membership, retry ->
            emit(HomeUiState.Loading)

            val (group, members, rankings, memberMissions) = coroutineScope {
                awaitAll(
                    async {
                        getMyGroupUseCase()
                            .getOrThrow()
                    },
                    async {
                        getMyGroupMembersUseCase()
                            .getOrThrow()
                    },
                    async {
                        getMyGroupLeaderboardUseCase()
                            .getOrThrow()
                    },
                    async {
                        getMyGroupMembersMissionsUseCase()
                            .getOrThrow()
                    }
                )
            }

            observeCurrentUserUseCase()
                .filterNotNull()
                .combine(observeMyMissionsThisWeekUseCase()) { user, myMissions ->
                    val user = (members as List<Member>)
                        .first { member -> member.id == user.id }
                        .copy(displayName = user.displayName, avatar = user.avatar)

                    HomeUiState.Success(
                        user,
                        group as Group,
                        members - user,
                        rankings as List<LeaderboardMember>,
                        myMissions,
                        memberMissions as List<Mission>,
                    )
                }.catch { throwable ->
                    emit(HomeUiState.Failure(throwable))
                }.collect(this)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState.Loading)

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent: SharedFlow<HomeUiEvent> = _uiEvent.asSharedFlow()

    fun completeMission(missionId: MissionId) {
        viewModelScope.launch {
            completeMissionUseCase(missionId)
                .onSuccess { mission ->
                    _uiEvent.emit(HomeUiEvent.MissionCompleted(mission))
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
            cancelMissionCompletionUseCase(missionId)
                .onSuccess { mission ->
                    _uiEvent.emit(HomeUiEvent.MissionCompletionCanceled(mission))
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
        val user: Member,
        val group: Group,
        val members: List<Member>,
        val rankings: List<LeaderboardMember>,
        val myMissions: List<Mission>,
        val memberMissions: List<Mission>,
    ) : HomeUiState

    @Immutable
    data class Failure(val throwable: Throwable) : HomeUiState
}