package it.stamp.domain.usecase.mission

import it.stamp.domain.exception.MissionNotCompletedException
import it.stamp.domain.exception.MissionNotFoundException
import it.stamp.domain.exception.NotAuthenticatedException
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
            val currentUser = authService.currentUser
                ?: throw NotAuthenticatedException()

            val mission = missionRepository.findById(missionId)
                ?: throw MissionNotFoundException()

            require(mission.assignee == currentUser.id) {
                throw UnauthorizedException()
            }

            require(mission.status == MissionStatus.COMPLETED) {
                throw MissionNotCompletedException()
            }

            missionRepository.update(mission.assign())
        }
}