package it.stamp.domain.usecase.mission

import it.stamp.domain.repository.MissionRepository
import it.stamp.domain.usecase.membership.ObserveMyMembershipUseCase
import it.stamp.model.mission.Mission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMyMissionsForThisWeek @Inject constructor(
    private val observeMyMembershipUseCase: ObserveMyMembershipUseCase,
    private val missionRepository: MissionRepository,
) {
    operator fun invoke(): Flow<List<Mission>> = observeMyMembershipUseCase()
        .flatMapLatest { membership ->
            if (membership == null) {
                flowOf(emptyList())
            } else {
                with(membership) {
                    missionRepository.observeMissionsByAssigneeThisWeek(groupId, assigneeId = userId)
                }
            }
        }
}