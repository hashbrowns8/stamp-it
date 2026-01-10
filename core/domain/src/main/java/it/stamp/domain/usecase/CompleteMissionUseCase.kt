package it.stamp.domain.usecase

import it.stamp.domain.repository.MissionRepository
import it.stamp.model.ids.MissionId
import it.stamp.model.mission.Mission
import it.stamp.model.mission.MissionStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompleteMissionUseCase @Inject constructor(
    private val missionRepository: MissionRepository,
) {
    suspend operator fun invoke(
        missionId: MissionId
    ): Result<Mission> = runCatching {
        missionRepository.updateMissionStatus(missionId, MissionStatus.COMPLETED)
    }
}