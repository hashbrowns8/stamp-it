package it.stamp.model.membership

import it.stamp.model.ids.UserId

data class Member(
    val id: UserId,
    val nickname: String,
    val avatarUrl: String?,
)