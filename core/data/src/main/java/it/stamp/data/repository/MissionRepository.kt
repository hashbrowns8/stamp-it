package it.stamp.data.repository

import it.stamp.data.firestore.mapper.toDomainModel
import it.stamp.data.firestore.mapper.toFirestoreModel
import it.stamp.data.firestore.source.MissionFirestoreDataSource
import it.stamp.domain.repository.MissionRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MissionDataRepository @Inject constructor(
    private val dataSource: MissionFirestoreDataSource,
) : MissionRepository {

    override suspend fun findById(id: MissionId): Mission? =
        dataSource.find(id.value)?.toDomainModel()

    override fun observeMissionsByAssignee(
        groupId: GroupId,
        assigneeId: UserId,
        dueWithinDays: Int?,
    ): Flow<List<Mission>> = dataSource.observeMissionsByAssignee(groupId, assigneeId, dueWithinDays)
        .map { missions ->
            missions.map { mission ->
                mission.toDomainModel()
            }
        }

    override fun observeMissionsByAssigner(
        groupId: GroupId,
        assignerId: UserId
    ): Flow<List<Mission>> = dataSource.observeMissionsByAssigner(groupId, assignerId)
        .map { missions ->
            missions.map { mission ->
                mission.toDomainModel()
            }
        }

    override suspend fun update(mission: Mission): Mission {
        val missionFirestore = mission.toFirestoreModel()

        dataSource.update(mission.id.value, missionFirestore)

        return getById(mission.id)
    }
}