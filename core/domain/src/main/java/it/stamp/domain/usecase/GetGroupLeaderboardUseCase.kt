package it.stamp.domain.usecase

import it.stamp.domain.service.LeaderboardService
import it.stamp.model.ids.GroupId
import it.stamp.model.stamp.LeaderboardMember
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetGroupLeaderboardUseCase @Inject constructor(
    private val leaderboardService: LeaderboardService
) {
    suspend operator fun invoke(groupId: GroupId): Result<List<LeaderboardMember>> =
        leaderboardService.getGroupLeaderboard(groupId)
}