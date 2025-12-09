package it.stamp.model.user

import it.stamp.model.ids.UserId

data class User(
    val id: UserId,
    val nickname: String,
    val avatarUrl: String?,
)