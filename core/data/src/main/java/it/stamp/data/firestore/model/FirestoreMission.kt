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
) {
    companion object {
        const val FIELD_GROUP_ID = "groupId"
        const val FIELD_ASSIGNED_TO = "assignedTo"
    }
}