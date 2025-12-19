package it.stamp.data.firestore.mapper

import com.google.firebase.auth.FirebaseUser
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.model.ids.UserId
import it.stamp.model.user.User

object UserMapper {
    fun toDomainModel(user: FirebaseUser) = with(user) {
        User(
            id = UserId(uid),
            displayName = displayName ?: String(),
            avatar = photoUrl?.toString() ?: String(),
        )
    }

    fun toDomainModel(user: FirestoreUser) = with(user) {
        User(
            id = UserId(userId),
            displayName = nickname,
            avatar = profileImage,
        )
    }
}