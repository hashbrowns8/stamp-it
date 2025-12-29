package it.stamp.model.user

import it.stamp.model.ids.UserId
import kotlin.time.Clock
import kotlin.time.Instant

data class User(
    val id: UserId,
    val displayName: String,
    val avatar: String,
    val createdAt: Instant = Clock.System.now(),
)