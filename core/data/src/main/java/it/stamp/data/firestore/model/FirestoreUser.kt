package it.stamp.data.firestore.model

import com.google.firebase.Timestamp

data class FirestoreUser(
    val userId: String = String(),
    val groupId: String = String(),
    val nickname: String = String(),
    val profileImage: String = DEFAULT_PROFILE_IMAGE,
    val nicknameChangedAt: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
) {
    companion object {
        const val FIELD_USER_ID = "userId"
        const val FIELD_GROUP_ID = "groupId"
        const val FIELD_NICKNAME = "nickname"
        const val FIELD_NICKNAME_CHANGED_AT = "nicknameChangedAt"
        const val FIELD_PROFILE_IMAGE = "profileImage"
        const val FIELD_CREATED_AT = "createdAt"
        const val DEFAULT_PROFILE_IMAGE = "profileImage1"
    }
}