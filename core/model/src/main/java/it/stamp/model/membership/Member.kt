package it.stamp.model.membership

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import kotlin.time.Instant

data class Member(
    val id: UserId,
    val groupId: GroupId,
    val displayName: String,
    val avatar: String,
    val role: Role,
    val joinedAt: Instant,
)  {
    val isLeader: Boolean
        get() = role == Role.LEADER
}