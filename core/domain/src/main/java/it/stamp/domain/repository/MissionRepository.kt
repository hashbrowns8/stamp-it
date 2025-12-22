package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission

interface MissionRepository {
    suspend fun getMissionsByAssigner(assignerId: UserId, groupId: GroupId): Result<List<Mission>>
    suspend fun getMissionsByAssigneeThisWeek(assigneeId: UserId, groupId: GroupId): Result<List<Mission>>
}