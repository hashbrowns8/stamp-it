package it.stamp.model.stamp

import it.stamp.model.membership.Member

data class LeaderboardMember(
    val member: Member,
    val rank: Int,
    val stamps: Int,
)