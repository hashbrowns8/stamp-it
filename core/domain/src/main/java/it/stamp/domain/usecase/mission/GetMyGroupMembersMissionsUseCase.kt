package it.stamp.domain.usecase.mission

import it.stamp.domain.repository.MissionRepository
import it.stamp.domain.usecase.membership.GetMyMembershipUseCase
import it.stamp.model.mission.Mission
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMyGroupMembersMissionsUseCase @Inject constructor(
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val missionRepository: MissionRepository,
) {
    suspend operator fun invoke(): Result<List<Mission>> =
        runCatching {
            getMyMembershipUseCase()
                .getOrThrow()
                .let { membership ->
                    missionRepository.getMissionsByAssigner(membership.groupId, assignerId = membership.userId)
                }
        }
}