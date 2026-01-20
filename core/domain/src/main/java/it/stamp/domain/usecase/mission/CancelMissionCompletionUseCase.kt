package it.stamp.domain.usecase.mission

import it.stamp.domain.exception.MissionNotCompletedException
import it.stamp.domain.exception.MissionNotFoundException
import it.stamp.domain.exception.UnauthorizedException
import it.stamp.domain.repository.MissionRepository
import it.stamp.domain.service.AuthService
import it.stamp.model.ids.MissionId
import it.stamp.model.mission.Mission
import it.stamp.model.mission.MissionStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CancelMissionCompletionUseCase @Inject constructor(
    private val authService: AuthService,
    private val missionRepository: MissionRepository,
) {
    suspend operator fun invoke(missionId: MissionId): Result<Mission> =
        runCatching {
            val user = authService.requireCurrentUser()

            val mission = missionRepository.findById(missionId)
                ?: throw MissionNotFoundException()

            require(mission.assignee == user.id) {
                throw UnauthorizedException()
            }

            require(mission.status == MissionStatus.COMPLETED) {
                throw MissionNotCompletedException()
            }

            missionRepository.update(mission.assign())
        }
}