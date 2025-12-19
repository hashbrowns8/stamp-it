package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership

interface MembershipRepository {
    suspend fun getGroupMemberships(groupId: GroupId): List<Membership>

    suspend fun getUserMembership(userId: UserId): Membership
}