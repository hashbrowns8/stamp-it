package it.stamp.data.model

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MembershipId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Group
import it.stamp.model.membership.Membership
import it.stamp.model.membership.Role
import it.stamp.model.user.User
import kotlin.time.Instant

fun User.toFirestoreUser(groupId: GroupId): FirestoreUser =
    FirestoreUser(
        id,
        groupId,
        displayName,
        avatar
    )

fun FirebaseUser.toDomainUser() =
    User(
        id = UserId(uid),
        displayName = displayName ?: ":-/",
        avatar = photoUrl?.toString(),
    )

fun FirestoreUser.toDomainUser() =
    User(
        id = UserId(userId),
        displayName = nickname,
        avatar = profileImage,
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

fun Timestamp.toKotlinInstant() = Instant.fromEpochSeconds(seconds, nanoseconds.toLong())

fun FirestoreMembership.toDomainMembership(): Membership =
    Membership(
        id = MembershipId(membershipId),
        groupId = GroupId(groupId),
        userId = UserId(userId),
        role = if (isLeader) {
            Role.LEADER
        } else {
            Role.MEMBER
        },
        joinedAt = joinedAt.toKotlinInstant(),
    )