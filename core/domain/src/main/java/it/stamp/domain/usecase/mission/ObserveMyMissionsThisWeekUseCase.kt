package it.stamp.domain.usecase.mission

import it.stamp.domain.repository.MissionRepository
import it.stamp.domain.usecase.membership.ObserveMyMembershipUseCase
import it.stamp.model.mission.Mission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMyMissionsThisWeekUseCase @Inject constructor(
    private val observeMyMembershipUseCase: ObserveMyMembershipUseCase,
    private val missionRepository: MissionRepository,
) {
    operator fun invoke(): Flow<List<Mission>> = observeMyMembershipUseCase()
        .flatMapLatest { membership ->
            missionRepository.observeMissionsByAssigneeThisWeek(membership.groupId, assigneeId = membership.userId)
        }
}