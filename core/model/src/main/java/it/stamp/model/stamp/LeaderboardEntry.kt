package it.stamp.model.stamp

import it.stamp.model.membership.Member

data class LeaderboardEntry(
    val member: Member,
    val rank: Int,
    val stamps: Int,
)