package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow

interface MembershipRepository {

    fun observeGroupMemberships(groupId: GroupId): Flow<List<Membership>>

    suspend fun getGroupMemberships(groupId: GroupId): List<Membership>

    fun observeUserMembership(userId: UserId): Flow<Membership>

    suspend fun getUserMembership(userId: UserId): Membership
}