package it.stamp.model.membership

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MembershipId
import it.stamp.model.ids.UserId
import kotlin.time.Instant

// TODO : Status
data class Membership(
    val id: MembershipId,
    val groupId: GroupId,
    val userId: UserId,
    val role: Role,
    val joinedAt: Instant,
) {
    val isLeader: Boolean
        get() = role == Role.LEADER
}