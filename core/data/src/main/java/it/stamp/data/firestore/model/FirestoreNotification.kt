package it.stamp.data.firestore.model

import com.google.firebase.Timestamp

data class FirestoreNotification(
    val noticeId: String = String(),
    val title: String = String(),
    val description: String = String(),
    val category: String = String(),
    val createdAt: Timestamp = Timestamp.now(),
    val url: String = String(),
    val isRead: Boolean = false,
    val userId: String = String(),
) : FirestoreModel {
    override val id: String = noticeId
}