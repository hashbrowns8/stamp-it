package it.stamp.data.firestore.model

import com.google.firebase.Timestamp

data class FirestoreUser(
    val userId: String = String(),
    val groupId: String = String(),
    val nickname: String = String(),
    val profileImage: String = String(),
    val nicknameChangedAt: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
)