package it.stamp.home.core

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
import it.stamp.domain.usecase.ObserveCurrentMembershipUseCase
import it.stamp.domain.usecase.ObserveCurrentUserUseCase
import it.stamp.domain.usecase.ObserveMyMissionsThisWeekUseCase
import it.stamp.model.ids.MissionId
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.membership.Membership
import it.stamp.model.mission.Mission
import it.stamp.model.stamp.LeaderboardMember
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeCurrentMembershipUseCase: ObserveCurrentMembershipUseCase,
    private val getGroupByIdUseCase: GetGroupByIdUseCase,
    private val getGroupMembersUseCase: GetGroupMembersUseCase,
    private val getGroupLeaderboardUseCase: GetGroupLeaderboardUseCase,
    private val observeMyMissionsThisWeekUseCase: ObserveMyMissionsThisWeekUseCase,
    private val getMembersMissionsUseCase: GetMembersMissionsUseCase,
    private val completeMissionUseCase: CompleteMissionUseCase,
    private val cancelMissionCompletionUseCase: CancelMissionCompletionUseCase,
) : ViewModel() {

    private val user: Flow<User> = observeCurrentUserUseCase()
        .filterNotNull()

    private val membership: Flow<Membership> = observeCurrentMembershipUseCase()
        .filterNotNull()

    private val retry = MutableStateFlow(0)

    fun retry() {
        retry.value += 1
    }

    private fun <T> Result<T>.asFlow(): Flow<T> = flowOf(getOrThrow())

    val uiState: StateFlow<HomeUiState> = combineTransform(
        user,
        membership,
        retry
    ) { user, membership, _ ->
        emit(HomeUiState.Loading)

        combine(
            flow = getGroupByIdUseCase(membership.groupId).asFlow(),
            flow2 = getGroupMembersUseCase(membership.groupId).asFlow(),
            flow3 = getGroupLeaderboardUseCase(membership.groupId).asFlow(),
            flow4 = observeMyMissionsThisWeekUseCase(),
            flow5 = getMembersMissionsUseCase(assignerId = user.id, membership.groupId).asFlow(),
        ) { group, members, rankings, myMissions, membersMissions ->
            val me = members.first { member -> member.id == user.id }

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