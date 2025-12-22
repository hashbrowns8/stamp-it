package it.stamp.data.repository

import it.stamp.data.firestore.mapper.MissionMapper
import it.stamp.data.firestore.source.MissionFirestoreDataSource
import it.stamp.domain.repository.MissionRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission
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

    override suspend fun getMissionsByAssigneeThisWeek(
        assigneeId: UserId,
        groupId: GroupId
    ): Result<List<Mission>> = runCatching {
        dataSource
            .getMissionsByAssigneeThisWeek(assigneeId, groupId)
            .map(MissionMapper::toDomainModel)
    }
}