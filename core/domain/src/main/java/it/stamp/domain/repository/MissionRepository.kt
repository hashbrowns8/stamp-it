package it.stamp.domain.repository

import it.stamp.domain.exception.MissionNotFoundException
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission
import kotlinx.coroutines.flow.Flow

interface MissionRepository {
    suspend fun findById(id: MissionId): Mission?

    suspend fun getById(id: MissionId): Mission = findById(id) ?: throw MissionNotFoundException()

    fun observeMissionsByAssignee(
        groupId: GroupId,
        assigneeId: UserId,
        dueWithinDays: Int? = null,
    ): Flow<List<Mission>>

    fun observeMissionsByAssigner(groupId: GroupId, assignerId: UserId): Flow<List<Mission>>

    suspend fun update(mission: Mission): Mission
}