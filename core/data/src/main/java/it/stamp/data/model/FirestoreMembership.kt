package it.stamp.data.model

import com.google.firebase.Timestamp
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MembershipId
import it.stamp.model.ids.UserId

data class FirestoreMembership(
    val membershipId: String,
    val groupId: String,
    val userId: String,
    val isLeader: Boolean,
    val nickname: String,
    val profileImage: String,
    val joinedAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
) {
    constructor(
        id: MembershipId,
        groupId: GroupId,
        userId: UserId,
        isLeader: Boolean,
        displayName: String,
        avatar: String
    ) : this(
        membershipId = id.value,
        groupId = groupId.value,
        userId = userId.value,
        isLeader = isLeader,
        nickname = displayName,
        profileImage = avatar,
    )
}