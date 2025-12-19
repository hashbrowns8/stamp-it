package it.stamp.data.firestore.model

import com.google.firebase.Timestamp

data class FirestoreGroup(
    val groupId: String = String(),
    val leaderId: String = String(),
    val name: String = String(),
    val inviteCode: String = String(),
    val inviteCodeCreateAt: Timestamp = Timestamp.now(),
    val nameChangedAt: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
)