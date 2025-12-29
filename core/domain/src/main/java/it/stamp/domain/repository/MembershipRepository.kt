package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow

interface MembershipRepository {

    fun observeMembershipsByGroup(groupId: GroupId): Flow<List<Membership>>

    suspend fun getMembershipsByGroup(groupId: GroupId): Result<List<Membership>>

    fun observeMembershipByUser(userId: UserId): Flow<Membership>

    suspend fun getMembershipByUser(userId: UserId): Result<Membership>
}