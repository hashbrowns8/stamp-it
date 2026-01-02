package it.stamp.domain.usecase

import it.stamp.domain.repository.MissionRepository
import it.stamp.model.mission.Mission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMyMissionsThisWeekUseCase @Inject constructor(
    private val observeCurrentMembershipUseCase: ObserveCurrentMembershipUseCase,
    private val missionRepository: MissionRepository,
) {
    operator fun invoke(): Flow<List<Mission>> = observeCurrentMembershipUseCase()
        .flatMapLatest { membership ->
            if (membership == null) {
                flowOf(emptyList())
            } else {
                missionRepository.observeMissionsByAssigneeThisWeek(membership.userId, membership.groupId)
            }
        }
}