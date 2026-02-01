package it.stamp.domain.usecase.mission

import it.stamp.domain.exception.MissionUpdateException
import it.stamp.domain.repository.MissionRepository
import it.stamp.domain.service.AuthenticationService
import it.stamp.model.ids.MissionId
import it.stamp.model.mission.Mission
import it.stamp.model.mission.MissionStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompleteMission @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val missionRepository: MissionRepository,
) {
    suspend operator fun invoke(missionId: MissionId): Result<Mission> =
        runCatching {
            val userId = authenticationService.requireAuthenticated().userId

            val mission = missionRepository.getById(missionId)

            require(mission.assignee == userId) {
                throw MissionUpdateException.Unauthorized()
            }

            require(mission.status == MissionStatus.DONE) {
                throw MissionUpdateException.MissionAlreadyCompleted()
            }

            missionRepository.update(mission.complete())
        }
}