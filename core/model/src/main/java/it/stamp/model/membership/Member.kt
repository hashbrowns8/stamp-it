package it.stamp.model.membership

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import kotlin.time.Instant

data class Member(
    val id: UserId,
    val groupId: GroupId,
    val displayName: DisplayName,
    val avatar: Avatar,
    val role: Role,
    val joinedAt: Instant,
)  {
    val isLeader: Boolean
        get() = role == Role.LEADER
}