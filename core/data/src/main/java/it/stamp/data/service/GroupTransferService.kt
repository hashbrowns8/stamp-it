package it.stamp.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import it.stamp.data.firestore.mapper.AvatarMapper
import it.stamp.data.firestore.mapper.GroupMapper
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.missionsCollection
import it.stamp.data.firestore.util.stampsCollection
import it.stamp.domain.exception.GroupNotFoundException
import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.GroupTransferService
import it.stamp.model.membership.Group
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreGroupTransferService @Inject constructor(
    private val membershipRepository: MembershipRepository,
    private val firestore: FirebaseFirestore,
) : GroupTransferService {
    override suspend fun transferGroup(
        user: User,
        leavingGroup: Group,
        joiningGroup: Group,
    ): Group {
        val groupMemberships = membershipRepository.getGroupMemberships(leavingGroup.id)

        val userMembership = groupMemberships.find { it.userId == user.id }
            ?: throw MembershipNotFoundException()

        val isSoloGroup = groupMemberships.size == 1

        val newLeaderMembership by lazy {
            groupMemberships.minByOrNull { it.joinedAt }
        }

        with(firestore) {
            val stamps: QuerySnapshot = stampsCollection
                .whereEqualTo("groupId", leavingGroup.id.value)
                .whereEqualTo("userId", user.id.value)
                .get()
                .await()

            val missions: QuerySnapshot = missionsCollection
                .whereEqualTo("groupId", leavingGroup.id.value)
                .whereEqualTo("assignedTo", user.id.value)
                .get()
                .await()

            runBatch { batch ->
                with(batch) {
                    // clear user data in leaving group
                    stamps.documents
                        .map { document -> document.reference }
                        .forEach(::delete)

                    missions.documents
                        .map { document -> document.reference }
                        .forEach(::delete)

                    if (isSoloGroup) {
                        groupsCollection
                            .document(leavingGroup.id.value)
                            .let(::delete)
                    }

                    membershipsCollection
                        .document(userMembership.id.value)
                        .let(::delete)

                    // assign new leader
                    if (userMembership.isLeader) {
                        newLeaderMembership?.let { membership ->
                            groupsCollection
                                .document(membership.groupId.value)
                                .let { document ->
                                    update(document, "leaderId", membership.userId.value)
                                }

                            membershipsCollection
                                .document(membership.id.value)
                                .let { document ->
                                    update(document, "isLeader", true)
                                }
                        }
                    }

                    // set new membership
                    val newUserMembership = createFirestoreMembership(user, joiningGroup)

                    set(membershipsCollection.document(newUserMembership.membershipId), newUserMembership)
                }
            }.await()
        }

        val newUserMembership = membershipRepository.getUserMembership(user.id)

        val snapshot = firestore.groupsCollection
            .document(newUserMembership.groupId.value)
            .get()
            .await()

        return snapshot.toObject(FirestoreGroup::class.java)
            ?.let(GroupMapper::toDomainModel)
            ?: throw GroupNotFoundException()
    }

    private fun createFirestoreMembership(user: User, joiningGroup: Group): FirestoreMembership {
        val id = buildString {
            append(joiningGroup.id.value)
            append('_')
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
}