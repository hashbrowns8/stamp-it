package it.stamp.domain.usecase

import it.stamp.domain.repository.MissionRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMyMissionsThisWeekUseCase @Inject constructor(
    private val missionRepository: MissionRepository,
) {
    operator fun invoke(assigneeId: UserId, groupId: GroupId): Flow<List<Mission>> =
        missionRepository.observeMissionsByAssigneeThisWeek(assigneeId, groupId)
}