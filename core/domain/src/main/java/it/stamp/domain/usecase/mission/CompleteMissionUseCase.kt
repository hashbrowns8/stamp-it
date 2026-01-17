package it.stamp.domain.usecase.mission

import it.stamp.domain.exception.MissionAlreadyCompletedException
import it.stamp.domain.exception.UnauthorizedException
import it.stamp.domain.repository.MissionRepository
import it.stamp.domain.service.AuthService
import it.stamp.model.ids.MissionId
import it.stamp.model.mission.Mission
import it.stamp.model.mission.MissionStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompleteMissionUseCase @Inject constructor(
    private val authService: AuthService,
    private val missionRepository: MissionRepository,
) {
    suspend operator fun invoke(missionId: MissionId): Result<Mission> =
        runCatching {
            val user = authService.requireUser()

            val mission = missionRepository.getById(missionId)

            require(mission.assignee == user.id) {
                throw UnauthorizedException()
            }

            require(mission.status == MissionStatus.COMPLETED) {
                throw MissionAlreadyCompletedException()
            }

            missionRepository.update(mission.complete())
        }
}