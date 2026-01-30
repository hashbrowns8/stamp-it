package it.stamp.data.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.missionsCollection
import it.stamp.data.firestore.util.stampsCollection
import it.stamp.data.firestore.util.usersCollection
import it.stamp.domain.exception.UserNotFoundException
import it.stamp.domain.service.MembershipService
import it.stamp.model.membership.InviteCode
import it.stamp.model.membership.Membership
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreMembershipService @Inject constructor(
    private val firestore: FirebaseFirestore,
) : MembershipService {

    override suspend fun transferLeadership(from: Membership, to: Membership) {
        firestore.run {
            runBatch { batch ->
                // 기존 리더 권한 해제
                membershipsCollection
                    .document(from.id.value)
                    .let { documentReference ->
                        batch.update(documentReference, FIELD_IS_LEADER, false)
                    }

                // 새 리더 권한 부여
                membershipsCollection
                    .document(to.id.value)
                    .let { documentReference ->
                        batch.update(documentReference, FIELD_IS_LEADER, true)
                    }

                // 그룹 리더 ID 업데이트
                groupsCollection
                    .document(from.groupId.value)
                    .let { documentReference ->
                        batch.update(documentReference, FIELD_LEADER_ID, to.userId.value)
                    }
            }
        }.await()
    }

    override suspend fun removeMember(membership: Membership) {
        val (documents, user) = queryMemberDataForDeletion(membership)

        val (newGroup, newMembership) = createNewGroupAndMembership(user)

        firestore.run {
            runBatch { batch ->
                // 사용자 데이터 삭제
                documents.forEach { document ->
                    batch.delete(document.reference)
                }

                // 기존 멤버십 삭제
                membershipsCollection
                    .document(membership.id.value)
                    .let(batch::delete)

                // 새로운 개인 그룹 & 멤버십 생성
                groupsCollection
                    .document(newGroup.groupId)
                    .let { documentReference ->
                        batch.set(documentReference, newGroup)
                    }

                membershipsCollection
                    .document(newMembership.membershipId)
                    .let { documentReference ->
                        batch.set(documentReference, newMembership)
                    }

                // 업데이트 그룹 ID
                usersCollection
                    .document(user.userId)
                    .let { documentReference ->
                        batch.update(documentReference, FIELD_GROUP_ID, newGroup.groupId)
                    }
            }
        }.await()
    }

    private suspend fun queryMemberDataForDeletion(
        membership: Membership
    ): Pair<List<DocumentSnapshot>, FirestoreUser> =
        coroutineScope {
            val stamps = async {
                firestore.stampsCollection
                    .whereEqualTo(FIELD_GROUP_ID, membership.groupId.value)
                    .whereEqualTo(FIELD_USER_ID, membership.userId.value)
                    .get()
                    .await()
                    .documents
            }

            val missions = async {
                firestore.missionsCollection
                    .whereEqualTo(FIELD_GROUP_ID, membership.groupId.value)
                    .whereEqualTo(FIELD_ASSIGNED_TO, membership.userId.value)
                    .get()
                    .await()
                    .documents
            }

            val user = async {
                firestore.usersCollection
                    .document(membership.userId.value)
                    .get()
                    .await()
                    .toObject(FirestoreUser::class.java)
                    ?: throw UserNotFoundException()
            }

            (stamps.await() + missions.await()) to user.await()
        }

    private fun createNewGroupAndMembership(user: FirestoreUser): Pair<FirestoreGroup, FirestoreMembership> {
        val inviteCode = InviteCode()

        val group = FirestoreGroup(
            leaderId = user.userId,
            name = "${user.nickname}의 그룹",
            inviteCode = inviteCode.value,
        )

        val membershipId = buildString {
            append(group.groupId)
            append(MEMBERSHIP_ID_SEPARATOR)
            append(user.userId)
        }

        val membership = FirestoreMembership(
            membershipId,
            group.groupId,
            user.userId,
            isLeader = true,
            user.nickname,
            user.profileImage,
        )

        return group to membership
    }

    companion object {
        private const val FIELD_IS_LEADER = "isLeader"
        private const val FIELD_LEADER_ID = "leaderId"
        private const val FIELD_GROUP_ID = "groupId"
        private const val FIELD_USER_ID = "userId"
        private const val FIELD_ASSIGNED_TO = "assignedTo"
        private const val MEMBERSHIP_ID_SEPARATOR = '_'
    }
}