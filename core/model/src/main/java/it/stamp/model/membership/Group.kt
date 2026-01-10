package it.stamp.model.membership

import it.stamp.model.ids.GroupId
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Group(
    val id: GroupId = GroupId(Uuid.random().toString()),
    val name: String,
    val inviteCode: InviteCode = InviteCode(),
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant? = null,
)