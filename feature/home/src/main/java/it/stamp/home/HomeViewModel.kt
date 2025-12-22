@file:Suppress("UNCHECKED_CAST")

package it.stamp.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.GetGroupByIdUseCase
import it.stamp.domain.usecase.GetGroupLeaderboardUseCase
import it.stamp.domain.usecase.GetGroupMembersUseCase
import it.stamp.domain.usecase.GetMembersMissionsUseCase
import it.stamp.domain.usecase.GetMyMissionsThisWeekUseCase
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.membership.Membership
import it.stamp.model.mission.Mission
import it.stamp.model.stamp.LeaderboardMember
import it.stamp.model.user.User
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGroupByIdUseCase: GetGroupByIdUseCase,
    private val getGroupMembersUseCase: GetGroupMembersUseCase,
    private val getGroupLeaderboardUseCase: GetGroupLeaderboardUseCase,
    private val getMyMissionsThisWeekUseCase: GetMyMissionsThisWeekUseCase,
    private val getMembersMissionsUseCase: GetMembersMissionsUseCase,
) : ViewModel() {

    private val user = MutableStateFlow<User?>(null)
    private val membership = MutableStateFlow<Membership?>(null)

    val uiState = combine(user, membership) { user, membership ->
        if (user == null || membership == null) return@combine HomeUiState.Loading

        coroutineScope {
            val group = async {
                getGroupByIdUseCase(membership.groupId)
            }
            val members = async {
                getGroupMembersUseCase(membership.groupId)
            }
            val rankings = async {
                getGroupLeaderboardUseCase(membership.groupId)
            }
            val myMissions = async {
                getMyMissionsThisWeekUseCase(assigneeId = user.id, membership.groupId)
            }
            val membersMissions = async {
                getMembersMissionsUseCase(assignerId = user.id, membership.groupId)
            }

            awaitAll(group, members, rankings, myMissions, membersMissions)
                .map { it.getOrThrow() }
                .let {
                    val group = it[0] as Group
                    val members = it[1] as List<Member>
                    val rankings = it[2] as List<LeaderboardMember>
                    val myMissions = it[3] as List<Mission>
                    val membersMissions = it[4] as List<Mission>
                    val me = members.first { member -> member.id == user.id }

                    HomeUiState.Success(
                        me,
                        group,
                        members = members - me,
                        rankings,
                        myMissions,
                        membersMissions,
                    )
                }
        }
    }.catch { throwable ->
        Timber.d(throwable)
        if (throwable is IOException) {
            HomeUiState.Failure("네트워크 연결에 실패하였습니다")
        } else {
            HomeUiState.Failure("알 수 없는 오류가 발생하였습니다")
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState.Loading)

    fun setUserAndMembership(me: User, membership: Membership) {
        viewModelScope.launch {
            this@HomeViewModel.user.emit(me)
            this@HomeViewModel.membership.emit(membership)
        }
    }
}

@Immutable
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
    data class Failure(val message: String) : HomeUiState
}