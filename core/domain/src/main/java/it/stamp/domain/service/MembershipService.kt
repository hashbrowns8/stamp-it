package it.stamp.domain.service

import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Membership

interface MembershipService {

    suspend fun joinGroup(currentMembership: Membership, targetGroupId: GroupId)

    suspend fun leaveGroup(currentMembership: Membership)
}