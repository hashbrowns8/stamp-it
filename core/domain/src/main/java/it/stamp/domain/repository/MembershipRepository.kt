package it.stamp.domain.repository

import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow

interface MembershipRepository {

    suspend fun findUserMembership(userId: UserId): Membership?

    suspend fun getUserMembership(userId: UserId): Membership =
        findUserMembership(userId) ?: throw MembershipNotFoundException()

    fun observeUserMembership(userId: UserId): Flow<Membership?>

    suspend fun getGroupMemberships(groupId: GroupId): List<Membership>

    fun observeGroupMemberships(groupId: GroupId): Flow<List<Membership>>

    suspend fun getGroupMemberCount(groupId: GroupId): Int

    suspend fun updateMembership(membership: Membership)
}