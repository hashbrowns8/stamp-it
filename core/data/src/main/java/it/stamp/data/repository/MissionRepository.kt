package it.stamp.data.repository

import it.stamp.data.firestore.mapper.MissionMapper
import it.stamp.data.firestore.source.MissionFirestoreDataSource
import it.stamp.domain.repository.MissionRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission
import it.stamp.model.mission.MissionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MissionDataRepository @Inject constructor(
    private val dataSource: MissionFirestoreDataSource,
) : MissionRepository {
    override suspend fun getMissionsByAssigner(
        assignerId: UserId,
        groupId: GroupId
    ): Result<List<Mission>> = runCatching {
        dataSource
            .getMissionsByAssigner(assignerId, groupId)
            .map(MissionMapper::toDomainModel)
    }

    override fun observeMissionsByAssigneeThisWeek(
        assigneeId: UserId,
        groupId: GroupId
    ): Flow<List<Mission>> = dataSource
        .observeMissionsByAssigneeThisWeek(assigneeId, groupId)
        .map { missions ->
            missions.map(MissionMapper::toDomainModel)
        }

    override suspend fun updateMissionStatus(
        missionId: MissionId,
        status: MissionStatus
    ): Result<Mission> = runCatching {
        dataSource
            .updateMissionStatus(missionId, status)
            .let(MissionMapper::toDomainModel)
    }
}