package it.stamp.domain.usecase

import it.stamp.domain.repository.MissionRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMembersMissionsUseCase @Inject constructor(
    private val missionRepository: MissionRepository,
) {
    suspend operator fun invoke(assignerId: UserId, groupId: GroupId): Result<List<Mission>> =
        missionRepository.getMissionsByAssigner(assignerId, groupId)
}