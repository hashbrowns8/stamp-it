package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission
import it.stamp.model.mission.MissionStatus
import kotlinx.coroutines.flow.Flow

interface MissionRepository {
    suspend fun getMissionsByAssigner(assignerId: UserId, groupId: GroupId): Result<List<Mission>>
    fun observeMissionsByAssigneeThisWeek(assigneeId: UserId, groupId: GroupId): Flow<List<Mission>>
    suspend fun updateMissionStatus(missionId: MissionId, status: MissionStatus): Result<Mission>
}