package it.stamp.data.model

import com.google.firebase.auth.FirebaseUser
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Group
import it.stamp.model.membership.Membership
import it.stamp.model.user.User

fun User.toFirestoreUser(groupId: GroupId): FirestoreUser =
    FirestoreUser(
        id,
        groupId,
        displayName,
        avatar
    )

fun Group.toFirestoreGroup(leaderId: UserId): FirestoreGroup =
    FirestoreGroup(
        id,
        leaderId,
        name,
        inviteCode
    )

fun Membership.toFirestoreMembership(displayName: String, avatar: String?): FirestoreMembership =
    FirestoreMembership(
        id,
        groupId,
        userId,
        isLeader,
        displayName,
        avatar ?: FirestoreUser.DEFAULT_AVATAR,
    )

fun FirebaseUser.toDomainUser() =
    User(
        id = UserId(uid),
        displayName = displayName ?: ":-/",
        avatar = photoUrl?.toString(),
    )