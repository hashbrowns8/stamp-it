package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.util.toKotlinInstant
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode

object GroupMapper {
    fun toDomainModel(group: FirestoreGroup) = with(group) {
        Group(
            id = GroupId(groupId),
            name = name,
            inviteCode = InviteCode(inviteCode),
            createdAt = createdAt.toKotlinInstant(),
        )
    }
}