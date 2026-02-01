package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.model.ids.UserId
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import it.stamp.model.user.User

fun FirestoreUser.toDomainModel(): User {
    val avatar = profileImage.takeLast(1)
        .toIntOrNull()
        ?.let { number ->
            Avatar.entries.getOrNull(number - 1)
        }
        ?: Avatar.CHARACTER_1

    return User(
        id = UserId(userId),
        displayName = DisplayName(nickname),
        avatar = avatar,
        createdAt = createdAt.toKotlinInstant(),
    )
}