package it.stamp.data.firestore.util

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.model.FirestoreMission
import it.stamp.data.firestore.model.FirestoreStamp
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.domain.exception.UserNotFoundException
import it.stamp.model.ids.MembershipId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.InviteCode
import it.stamp.model.membership.Membership
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

suspend fun queryMemberDataForDeletion(
    firestore: FirebaseFirestore,
    membership: Membership
): List<DocumentSnapshot> = coroutineScope {
    awaitAll(
        async {
            firestore.stampsCollection
                .whereEqualTo(FirestoreStamp.FIELD_GROUP_ID, membership.groupId.value)
                .whereEqualTo(FirestoreStamp.FIELD_USER_ID, membership.userId.value)
                .get()
                .await()
                .documents
        },
        async {
            firestore.missionsCollection
                .whereEqualTo(FirestoreMission.FIELD_GROUP_ID, membership.groupId.value)
                .whereEqualTo(FirestoreMission.FIELD_ASSIGNED_TO, membership.userId.value)
                .get()
                .await()
                .documents
        }
    ).flatten()
}

fun WriteBatch.assignNewLeader(
    firestore: FirebaseFirestore,
    nextLeaderMembership: Membership,
) {
    firestore.groupsCollection
        .document(nextLeaderMembership.groupId.value)
        .let { documentReference ->
            update(documentReference, FirestoreGroup.FIELD_LEADER_ID, nextLeaderMembership.userId.value)
        }

    firestore.membershipsCollection
        .document(nextLeaderMembership.id.value)
        .let { documentReference ->
            update(documentReference, FirestoreMembership.FIELD_IS_LEADER, true)
        }
}

suspend fun createNewGroupAndMembership(
    firestore: FirebaseFirestore,
    membershipId: MembershipId,
    userId: UserId,
): Pair<FirestoreGroup, FirestoreMembership> {
    val user = firestore.usersCollection
        .document(userId.value)
        .get()
        .await()
        .toObject(FirestoreUser::class.java)
        ?: throw UserNotFoundException()

    val inviteCode = InviteCode()

    val group = FirestoreGroup(
        leaderId = userId.value,
        name = "${user.nickname}의 그룹",
        inviteCode = inviteCode.value,
    )

    val newMembership = createNewMembership(
        firestore,
        currentMembershipId = membershipId,
        userId.value,
        targetGroupId = group.groupId,
    )

    return group to newMembership
}

suspend fun createNewMembership(
    firestore: FirebaseFirestore,
    currentMembershipId: MembershipId,
    userId: String,
    targetGroupId: String,
): FirestoreMembership {
    val currentMembership = firestore.membershipsCollection
        .document(currentMembershipId.value)
        .get()
        .await()
        .toObject(FirestoreMembership::class.java)
        ?: throw MembershipNotFoundException()

    val membershipId = buildString {
        append(targetGroupId)
        append(FirestoreMembership.MEMBERSHIP_ID_SEPARATOR)
        append(userId)
    }

    return currentMembership.copy(
        membershipId = membershipId,
        groupId = targetGroupId,
        isLeader = false,
    )
}