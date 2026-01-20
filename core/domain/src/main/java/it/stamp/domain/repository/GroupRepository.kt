package it.stamp.domain.repository

import it.stamp.domain.exception.GroupNotFoundException
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    suspend fun findById(id: GroupId): Group?

    suspend fun getById(id: GroupId): Group = findById(id) ?: throw GroupNotFoundException()

    suspend fun findByInviteCode(inviteCode: InviteCode): Group?

    fun observe(id: GroupId): Flow<Group>

    suspend fun update(group: Group)
}