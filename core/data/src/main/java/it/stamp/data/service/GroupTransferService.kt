package it.stamp.data.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import it.stamp.data.firestore.mapper.AvatarMapper
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.missionsCollection
import it.stamp.data.firestore.util.stampsCollection
import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.domain.repository.GroupRepository
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.GroupTransferService
import it.stamp.model.membership.Group
import it.stamp.model.membership.Membership
import it.stamp.model.user.User
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.util.Collections
import javax.inject.Inject

class FirestoreGroupTransferService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val membershipRepository: MembershipRepository,
    private val groupRepository: GroupRepository,
) : GroupTransferService {

    override suspend fun transfer(
        user: User,
        leavingGroup: Group,
        joiningGroup: Group,
    ): Group {
        val transferContext = transferContext(user, leavingGroup)

        with(transferContext) {
            val documents = queryMemberDataForDeletion(userMembership)

            firestore.run {
                runBatch { batch ->
                    documents.forEach { document ->
                        batch.delete(document.reference)
                    }

                    if (isSoloGroup) {
                        groupsCollection
                            .document(leavingGroup.id.value)
                            .let(batch::delete)
                    }

                    if (newLeaderMembership != null) {
                        batch.assignNewLeader(newLeaderMembership)
                    }

                    val newUserMembership = createNewMembership(user, joiningGroup)

                    membershipsCollection
                        .document(newUserMembership.membershipId)
                        .let { documentReference ->
                            batch.set(documentReference, newUserMembership)
                        }
                }
            }.await()
        }

        return membershipRepository.getUserMembership(user.id)
            .let { membership ->
                groupRepository.getById(membership.groupId)
            }
    }

    data class TransferContext(
        val userMembership: Membership,
        val isSoloGroup: Boolean,
        val newLeaderMembership: Membership?,
    )

    private suspend fun transferContext(user: User, leavingGroup: Group): TransferContext {
        val groupMemberships = membershipRepository.getGroupMemberships(leavingGroup.id)

        val userMembership = groupMemberships.find { it.userId == user.id }
            ?: throw MembershipNotFoundException()

        val isSoloGroup = groupMemberships.size == 1

        val newLeaderMembership = if (userMembership.isLeader && !isSoloGroup) {
            groupMemberships
                .filter { !it.isLeader }
                .minByOrNull { it.joinedAt }
        } else {
            null
        }

        return TransferContext(
            userMembership,
            isSoloGroup,
            newLeaderMembership,
        )
    }

    private suspend fun queryMemberDataForDeletion(
        membership: Membership
    ): List<DocumentSnapshot> = coroutineScope {
        awaitAll(
            async {
                firestore.stampsCollection
                    .whereEqualTo(FIELD_GROUP_ID, membership.groupId.value)
                    .whereEqualTo(FIELD_USER_ID, membership.userId.value)
                    .get()
                    .await()
                    .documents
            },
            async {
                firestore.missionsCollection
                    .whereEqualTo(FIELD_GROUP_ID, membership.groupId.value)
                    .whereEqualTo(FIELD_ASSIGNED_TO, membership.userId.value)
                    .get()
                    .await()
                    .documents
            },
            async {
                firestore.membershipsCollection
                    .document(membership.id.value)
                    .get()
                    .await()
                    .let(Collections::singletonList)
            }
        ).flatten()
    }

    private fun WriteBatch.assignNewLeader(newLeaderMembership: Membership) {
        firestore.groupsCollection
            .document(newLeaderMembership.groupId.value)
            .let { documentReference ->
                update(documentReference, FIELD_LEADER_ID, newLeaderMembership.userId.value)
            }

        firestore.membershipsCollection
            .document(newLeaderMembership.id.value)
            .let { documentReference ->
                update(documentReference, FIELD_IS_LEADER, true)
            }
    }

    private fun createNewMembership(user: User, joiningGroup: Group): FirestoreMembership {
        val id = buildString {
            append(joiningGroup.id.value)
            append(MEMBERSHIP_ID_SEPARATOR)
            append(user.id.value)
        }

        return FirestoreMembership(
            id,
            joiningGroup.id.value,
            user.id.value,
            false,
            user.displayName.value,
            user.avatar.let(AvatarMapper::toFirestoreModel),
        )
    }

    companion object {
        private const val FIELD_GROUP_ID = "groupId"
        private const val FIELD_USER_ID = "userId"
        private const val FIELD_ASSIGNED_TO = "assignedTo"
        private const val FIELD_LEADER_ID = "leaderId"
        private const val FIELD_IS_LEADER = "isLeader"
        private const val MEMBERSHIP_ID_SEPARATOR = '_'
    }
}