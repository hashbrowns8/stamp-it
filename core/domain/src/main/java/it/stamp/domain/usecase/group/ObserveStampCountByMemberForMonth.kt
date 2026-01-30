package it.stamp.domain.usecase.group

import it.stamp.domain.service.LeaderboardService
import it.stamp.domain.usecase.membership.ObserveMyMembershipUseCase
import it.stamp.model.ids.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveStampCountByMemberForMonth @Inject constructor(
    private val observeMyMembershipUseCase: ObserveMyMembershipUseCase,
    private val leaderboardService: LeaderboardService,
) {
    operator fun invoke(): Flow<Map<UserId, Int>> =
        observeMyMembershipUseCase()
            .distinctUntilChangedBy { membership -> membership?.groupId }
            .flatMapLatest { membership ->
                if (membership == null) {
                    flowOf(emptyMap())
                } else {
                    leaderboardService.observeStampCountByMemberForMonth(membership.groupId)
                }
            }
}