package it.stamp.data.repository

import it.stamp.data.firestore.mapper.MissionMapper
import it.stamp.data.firestore.source.MissionFirestoreDataSource
import it.stamp.domain.exception.MissionNotFoundException
import it.stamp.domain.repository.MissionRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MissionDataRepository @Inject constructor(
    private val firestoreDataSource: MissionFirestoreDataSource,
) : MissionRepository {

    override suspend fun findById(id: MissionId): Mission? =
        firestoreDataSource.get(id.value)
            ?.let(MissionMapper::toDomainModel)

    override suspend fun getMissionsByAssigner(
        groupId: GroupId,
        assignerId: UserId
    ): List<Mission> = firestoreDataSource.getMissionsByAssigner(groupId, assignerId)
        .map(MissionMapper::toDomainModel)

    override fun observeMissionsByAssigneeThisWeek(
        groupId: GroupId,
        assigneeId: UserId
    ): Flow<List<Mission>> = firestoreDataSource.observeMissionsByAssigneeThisWeek(groupId, assigneeId)
        .map { missions ->
            missions.map(MissionMapper::toDomainModel)
        }

    override suspend fun update(mission: Mission): Mission {
        val missionFirestore = MissionMapper.toFirestoreModel(mission)

        firestoreDataSource.update(mission.id.value, missionFirestore)

        return findById(mission.id) ?: throw MissionNotFoundException()
    }
}