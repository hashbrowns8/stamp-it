package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.toKotlinInstant
import it.stamp.model.ids.UserId
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import it.stamp.model.user.User

object UserMapper {
    fun toDomainModel(user: FirestoreUser) = with(user) {
        val avatar = parseAvatar(profileImage)

        User(
            id = UserId(userId),
            displayName = DisplayName(nickname),
            avatar,
            createdAt = createdAt.toKotlinInstant(),
        )
    }

    private fun parseAvatar(profileImage: String): Avatar =
        profileImage.takeLast(1)
            .toInt()
            .let(Avatar::valueOf)
}