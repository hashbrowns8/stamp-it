package it.stamp.domain.usecase.group

import it.stamp.domain.service.LeaderboardService
import it.stamp.model.stamp.LeaderboardMember
import javax.inject.Inject

class GetMyGroupLeaderboardUseCase @Inject constructor(
    private val getMyGroupUseCase: GetMyGroupUseCase,
    private val leaderboardService: LeaderboardService,
) {
    suspend operator fun invoke(): Result<List<LeaderboardMember>> =
        runCatching {
            getMyGroupUseCase()
                .getOrThrow()
                .let { group ->
                    leaderboardService.getGroupLeaderboard(group.id)
                }
        }
}