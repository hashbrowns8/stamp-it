package it.stamp.data.model

import com.google.firebase.Timestamp
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.InviteCode

data class FirestoreGroup(
    val groupId: String = String(),
    val leaderId: String = String(),
    val name: String = String(),
    val inviteCode: String = String(),
    val inviteCodeCreateAt: Timestamp = Timestamp.now(),
    val nameChangedAt: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
) {
    constructor(
        id: GroupId,
        leaderId: UserId,
        name: String,
        inviteCode: InviteCode,
    ) : this(
        id.value,
        leaderId.value,
        name,
        inviteCode.value
    )
}