package it.stamp.model.stamp

import it.stamp.model.ids.UserId

data class LeaderboardMember(
    val id: UserId,
    val displayName: String,
    val avatar: String,
    val rank: Int,
    val stamps: Int,
)