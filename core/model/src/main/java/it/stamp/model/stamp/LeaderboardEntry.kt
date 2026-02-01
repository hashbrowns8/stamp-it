package it.stamp.model.stamp

import it.stamp.model.ids.UserId

data class LeaderboardEntry(
    val memberId: UserId,
    val rank: Int,
    val stampCount: Int,
)