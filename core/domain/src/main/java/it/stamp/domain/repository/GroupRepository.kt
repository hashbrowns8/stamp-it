package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group

interface GroupRepository {

    suspend fun findById(id: GroupId): Group?
}