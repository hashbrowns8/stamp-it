package it.stamp.data.service

import it.stamp.domain.exception.MemberNotFoundException
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.MemberService
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Member
import it.stamp.model.membership.Membership
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class FirestoreMemberService @Inject constructor(
    private val membershipRepository: MembershipRepository,
    private val userRepository: UserRepository,
) : MemberService {

    override fun observeMembersByUser(userId: UserId): Flow<List<Member>> = membershipRepository.observeMembershipByUser(userId)
        .flatMapLatest { membership ->
            membershipRepository.observeMembershipsByGroup(membership.groupId)
        }
        .mapLatest { memberships ->
            memberships.map { membership ->
                getMemberByMembership(membership)
            }
        }
        .flowOn(Dispatchers.IO)

    override suspend fun getMembersByGroup(groupId: GroupId): List<Member> {
        return membershipRepository.getMembershipsByGroup(groupId)
            .map { membership ->
                getMemberByMembership(membership)
            }
    }

    private suspend fun getMemberByMembership(membership: Membership): Member {
        val user = userRepository.findById(membership.userId)
            ?: throw MemberNotFoundException()

        return Member(
            user.id,
            membership.groupId,
            user.displayName,
            user.avatar,
            membership.role,
            membership.joinedAt,
        )
    }
}