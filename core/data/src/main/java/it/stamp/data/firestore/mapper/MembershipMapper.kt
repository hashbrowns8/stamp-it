package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MembershipId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership
import it.stamp.model.membership.Role

fun FirestoreMembership.toDomainModel(): Membership {
    return Membership(
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
}