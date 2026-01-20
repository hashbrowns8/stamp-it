package it.stamp.data.firestore.model

import com.google.firebase.Timestamp

data class FirestoreUser(
    val userId: String = String(),
    val groupId: String = String(),
    val nickname: String = String(),
    val profileImage: String = PROFILE_IMAGE_1,
    val nicknameChangedAt: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
) {
    companion object {
        private const val PROFILE_IMAGE_1 = "profileImage1"
    }
}