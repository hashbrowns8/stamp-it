package it.stamp.model.membership

import it.stamp.model.ids.GroupId
import kotlin.time.Instant

data class Group(
    val id: GroupId,
    val name: String,
    val invitationCode: String,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
)