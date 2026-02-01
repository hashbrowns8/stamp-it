package it.stamp.domain.service

import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Membership

interface LeadershipService {

    suspend fun renameGroup(groupId: GroupId, groupName: String)

    suspend fun transferLeadership(from: Membership, to: Membership)

    suspend fun removeMember(membership: Membership)
}