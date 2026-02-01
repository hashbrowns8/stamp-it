package it.stamp.data.firestore.model

import com.google.firebase.Timestamp

data class FirestoreMembership(
    val membershipId: String = String(),
    val groupId: String = String(),
    val userId: String = String(),
    @field:JvmField
    val isLeader: Boolean = false,
    val nickname: String = String(),
    val profileImage: String = String(),
    val joinedAt: Timestamp = Timestamp.now(),
) {
    companion object {
        const val FIELD_MEMBERSHIP_ID = "membershipId"
        const val FIELD_GROUP_ID = "groupId"
        const val FIELD_USER_ID = "userId"
        const val FIELD_IS_LEADER = "isLeader"
        const val MEMBERSHIP_ID_SEPARATOR = '_'
    }
}