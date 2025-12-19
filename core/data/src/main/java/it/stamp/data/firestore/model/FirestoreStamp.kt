package it.stamp.data.firestore.model

import com.google.firebase.Timestamp

data class FirestoreStamp(
    val stampId: String = String(),
    val userId: String = String(),
    val groupId: String = String(),
    val missionId: String = String(),
    val type: String = String(),
    val pinNumber: Int = 1,
    val month: String = String(),
    val assignedBy: String = String(),
    val createdAt: Timestamp = Timestamp.now(),
    val maxStamps: Int = 30,
)