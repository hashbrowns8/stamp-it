package it.stamp.data.service

import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.MemberService
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Member
import it.stamp.model.membership.Membership
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultMemberService @Inject constructor(
    private val membershipRepository: MembershipRepository,
    private val userRepository: UserRepository,
) : MemberService {

    override fun observeMembers(groupId: GroupId): Flow<List<Member>> =
        membershipRepository.observeGroupMemberships(groupId)
            .map { memberships ->
                membersFromMemberships(memberships)
            }

    override suspend fun getMembers(groupId: GroupId): List<Member> {
        return membersFromMemberships(memberships = membershipRepository.getGroupMemberships(groupId))
    }

    private suspend fun membersFromMemberships(memberships: List<Membership>): List<Member> {
        val userIds = memberships.map(Membership::userId)

        val users = userRepository.getByIds(userIds)

        val usersById = users.associateBy(User::id)

        return memberships.map { membership ->
            val user = usersById.getValue(membership.userId)

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
}