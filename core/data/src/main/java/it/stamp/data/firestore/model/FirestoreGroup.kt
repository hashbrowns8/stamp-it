package it.stamp.data.firestore.model

import com.google.firebase.Timestamp

data class FirestoreGroup(
    val groupId: String,
    val leaderId: String,
    val name: String,
    val inviteCode: String,
    val inviteCodeCreateAt: Timestamp = Timestamp.now(),
    val nameChangedAt: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
)