package it.stamp.data.firestore.mapper

import it.stamp.model.user.Avatar

object AvatarMapper {


    fun toDomainModel(value: String): Avatar =
        value.takeLast(1)
            .toIntOrNull()
            ?.let { number ->
                Avatar.entries.getOrNull(number - 1)
            }
            ?: Avatar.CHARACTER_1

    fun toFirestoreModel(avatar: Avatar): String = PROFILE_IMAGE.plus(avatar.ordinal + 1)
}

const val PROFILE_IMAGE = "profileImage"



fun Avatar.toFirestoreModel(): String = PROFILE_IMAGE.plus(ordinal + 1)