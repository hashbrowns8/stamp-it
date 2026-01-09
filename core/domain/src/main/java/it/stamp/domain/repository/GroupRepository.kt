package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode

interface GroupRepository {

    suspend fun getGroupById(groupId: GroupId): Result<Group>

    suspend fun getGroupByInviteCode(inviteCode: InviteCode): Result<Group>
}