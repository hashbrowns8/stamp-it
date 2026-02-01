package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode

fun FirestoreGroup.toDomainModel(): Group {
    return Group(
        id = GroupId(groupId),
        name = name,
        inviteCode = InviteCode(inviteCode),
        createdAt = createdAt.toKotlinInstant(),
    )
}