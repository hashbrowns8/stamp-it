package it.stamp.data.firestore.model

import com.google.firebase.Timestamp
import kotlin.uuid.Uuid

data class FirestoreGroup(
    val groupId: String = Uuid.random().toString(),
    val leaderId: String = String(),
    val name: String = String(),
    val nameChangedAt: Timestamp = Timestamp.now(),
    val inviteCode: String = String(),
    val inviteCodeCreateAt: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
) {
    companion object {
        const val FIELD_LEADER_ID = "leaderId"
    }
}