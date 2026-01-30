package it.stamp.data.service

import it.stamp.common.di.ApplicationScope
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.service.GroupMemberService
import it.stamp.model.membership.Member
import it.stamp.model.membership.Membership
import it.stamp.model.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

class DefaultGroupMemberService @Inject constructor(
    authenticationService: AuthenticationService,
    private val membershipRepository: MembershipRepository,
    private val userRepository: UserRepository,
    @ApplicationScope coroutineScope: CoroutineScope,
) : GroupMemberService {

    private val myGroupMembers: StateFlow<List<Member>> = authenticationService.currentUser
        .distinctUntilChangedBy { user -> user?.id }
        .flatMapLatest { user ->
            if (user == null) {
                flowOf(null)
            } else {
                membershipRepository.observeUserMembership(user.id)
            }
        }
        .distinctUntilChangedBy { membership -> membership?.groupId }
        .flatMapLatest { membership ->
            if (membership == null) {
                flowOf(emptyList())
            } else {
                membershipRepository.observeGroupMemberships(membership.groupId)
                    .filterNot(List<Membership>::isEmpty)
                    .map { memberships ->
                        membersFromMemberships(memberships)
                    }
            }
        }
        .catch { throwable ->
            Timber.d(throwable)

            emit(emptyList())
        }
        .stateIn(coroutineScope, SharingStarted.Eagerly, emptyList())

    private suspend fun membersFromMemberships(memberships: List<Membership>): List<Member> {
        val usersByUserId = memberships.map(Membership::userId)
            .let { ids ->
                userRepository.getByIds(ids)
            }
            .associateBy(User::id)

        return memberships.map { membership ->
            val user = usersByUserId.getValue(membership.userId)

            Member(
                user.id,
                membership.groupId,
                user.displayName,
                user.avatar,
                membership.role,
                membership.joinedAt,
            )
        }
    }

    override fun observeMyGroupMembers(): Flow<List<Member>> = myGroupMembers

    override suspend fun getMyGroupMembers(): List<Member> = myGroupMembers.value
}