package it.stamp.domain.repository

import it.stamp.domain.exception.GroupNotFoundException
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode

interface GroupRepository {
    suspend fun findGroupByInviteCode(inviteCode: InviteCode): Group?

    suspend fun findById(id: GroupId): Group?

    suspend fun getById(id: GroupId): Group = findById(id) ?: throw GroupNotFoundException()
}