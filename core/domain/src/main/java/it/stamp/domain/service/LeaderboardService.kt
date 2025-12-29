package it.stamp.domain.service

import it.stamp.model.ids.GroupId
import it.stamp.model.stamp.LeaderboardMember

interface LeaderboardService {
    suspend fun getGroupLeaderboard(groupId: GroupId): Result<List<LeaderboardMember>>
}