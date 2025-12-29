package it.stamp.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.CancelMissionCompletionUseCase
import it.stamp.domain.usecase.CompleteMissionUseCase
import it.stamp.domain.usecase.GetGroupByIdUseCase
import it.stamp.domain.usecase.GetGroupLeaderboardUseCase
import it.stamp.domain.usecase.GetGroupMembersUseCase
import it.stamp.domain.usecase.GetMembersMissionsUseCase
import it.stamp.domain.usecase.ObserveMyMissionsThisWeekUseCase
import it.stamp.model.ids.MissionId
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.membership.Membership
import it.stamp.model.mission.Mission
import it.stamp.model.stamp.LeaderboardMember
import it.stamp.model.user.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGroupByIdUseCase: GetGroupByIdUseCase,
    private val getGroupMembersUseCase: GetGroupMembersUseCase,
    private val getGroupLeaderboardUseCase: GetGroupLeaderboardUseCase,
    private val observeMyMissionsThisWeekUseCase: ObserveMyMissionsThisWeekUseCase,
    private val getMembersMissionsUseCase: GetMembersMissionsUseCase,
    private val completeMissionUseCase: CompleteMissionUseCase,
    private val cancelMissionCompletionUseCase: CancelMissionCompletionUseCase,
) : ViewModel() {

    private val user = MutableStateFlow<User?>(null)
    private val membership = MutableStateFlow<Membership?>(null)

    fun setUserAndMembership(user: User, membership: Membership) {
        this@HomeViewModel.user.value = user
        this@HomeViewModel.membership.value = membership
    }

    private val retry = MutableStateFlow(0)

    fun retry() {
        retry.value += 1
    }

    val uiState = combineTransform(
        user,
        membership,
        retry
    ) { user, membership, _ ->
        if (user == null || membership == null) return@combineTransform

        emit(HomeUiState.Loading)

        combine(
            flow = flowOf(getGroupByIdUseCase(membership.groupId)),
            flow2 = flowOf(getGroupMembersUseCase(membership.groupId)),
            flow3 = flowOf(getGroupLeaderboardUseCase(membership.groupId)),
            flow4 = observeMyMissionsThisWeekUseCase(assigneeId = user.id, membership.groupId),
            flow5 = flowOf(getMembersMissionsUseCase(assignerId = user.id, membership.groupId)),
        ) { group, members, rankings, myMissions, membersMissions ->
            throw Exception() // TODO

            val group = group.getOrElse { throwable -> throw throwable }
            val members = members.getOrElse { throwable -> throw throwable }
            val me = members.first { member -> member.id == user.id }
            val rankings = rankings.getOrElse { throwable -> throw throwable }
            val membersMissions = membersMissions.getOrElse { throwable -> throw throwable }

            HomeUiState.Success(
                user = me,
                group,
                members = members - me,
                rankings,
                myMissions,
                membersMissions,
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
                .onFailure(::handleFailure)
        }
    }

    fun cancelMissionCompletion(missionId: MissionId) {
        viewModelScope.launch {
            cancelMissionCompletionUseCase(missionId)
                .onSuccess { mission ->
                    _uiEvent.emit(HomeUiEvent.MissionCompletionCanceled(mission))
                }
                .onFailure(::handleFailure)
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
        val membersMissions: List<Mission>,
    ) : HomeUiState

    @Immutable
    data class Failure(val throwable: Throwable) : HomeUiState
}