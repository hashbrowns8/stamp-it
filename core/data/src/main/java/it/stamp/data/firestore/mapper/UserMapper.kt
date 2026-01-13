package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.toKotlinInstant
import it.stamp.model.ids.UserId
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import it.stamp.model.user.User

object UserMapper {
    fun toDomainModel(user: FirestoreUser) = with(user) {
        val avatar = AvatarMapper.toDomainModel(profileImage)

        User(
            id = UserId(userId),
            displayName = DisplayName(nickname),
            avatar,
            createdAt = createdAt.toKotlinInstant(),
        )
    }
}

object AvatarMapper {
    private const val PROFILE_IMAGE = "profileImage"

    fun toDomainModel(value: String): Avatar =
        value.takeLast(1)
            .toIntOrNull()
            ?.let { number ->
                Avatar.entries.getOrNull(number - 1)
            }
            ?: Avatar.CHARACTER_1

    fun toFirestoreModel(avatar: Avatar): String = PROFILE_IMAGE.plus(avatar.ordinal + 1)
}