package it.stamp.domain.service

import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.repository.UserRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Member
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class MemberService @Inject constructor(
    private val membershipRepository: MembershipRepository,
    private val userRepository: UserRepository,
) {
    fun observeUserMembers(userId: UserId): Flow<List<Member>> = membershipRepository.observeUserMembership(userId)
        .flatMapLatest { membership ->
            membership?.groupId
                ?.let(membershipRepository::observeGroupMemberships)
                ?: flowOf(emptyList())
        }
        .mapLatest { memberships ->
            memberships.mapNotNull { membership ->
                findMemberByMembership(membership)
            }
        }

    private suspend fun findMemberByMembership(membership: Membership): Member? {
        val user = userRepository.findById(membership.userId)
            ?: return null

        return Member(
            user.id,
            membership.groupId,
            user.displayName,
            user.avatar,
            membership.role,
            membership.joinedAt,
        )
    }

    suspend fun getGroupMembers(groupId: GroupId): List<Member> =
        membershipRepository.getGroupMemberships(groupId)
            .mapNotNull { membership ->
                findMemberByMembership(membership)
            }

    suspend fun getGroupMemberCount(groupId: GroupId): Int =
        membershipRepository.getGroupMemberCount(groupId)
}