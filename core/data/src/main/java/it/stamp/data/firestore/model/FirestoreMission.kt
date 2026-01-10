package it.stamp.data.firestore.model

import com.google.firebase.Timestamp

data class FirestoreMission(
    val missionId: String = String(),
    val groupId: String = String(),
    val title: String = String(),
    val assignedBy: String = String(),
    val assignedTo: String = String(),
    val createDate: Timestamp = Timestamp.now(),
    val dueDate: Timestamp = Timestamp.now(),
    val category: String = String(),
    val status: String = String(),
    val missionType: String = String(),
) : FirestoreModel {
    override val id: String
        get() = missionId
}