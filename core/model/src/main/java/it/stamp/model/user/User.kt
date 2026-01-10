package it.stamp.model.user

import it.stamp.model.ids.UserId
import kotlin.time.Clock
import kotlin.time.Instant

data class User(
    val id: UserId,
    val displayName: DisplayName,
    val avatar: Avatar = Avatar.CHARACTER_1,
    val createdAt: Instant = Clock.System.now(),
)