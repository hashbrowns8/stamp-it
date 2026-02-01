package it.stamp.data.service

import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.mapper.toFirestoreModel
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.queryMemberDataForDeletion
import it.stamp.data.firestore.util.usersCollection
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.LeadershipService
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.InviteCode
import it.stamp.model.membership.Membership
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreLeadershipService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository,
) : LeadershipService {

    override suspend fun renameGroup(
        groupId: GroupId,
        groupName: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun transferLeadership(from: Membership, to: Membership) {
        firestore.run {
            runBatch { batch ->
                // 기존 리더 권한 해제
                membershipsCollection
                    .document(from.id.value)
                    .let { documentReference ->
                        batch.update(documentReference, FirestoreMembership.FIELD_IS_LEADER, false)
                    }

                // 새 리더 권한 부여
                membershipsCollection
                    .document(to.id.value)
                    .let { documentReference ->
                        batch.update(documentReference, FirestoreMembership.FIELD_IS_LEADER, true)
                    }

                // 그룹 리더 ID 업데이트
                groupsCollection
                    .document(from.groupId.value)
                    .let { documentReference ->
                        batch.update(documentReference, FirestoreGroup.FIELD_LEADER_ID, to.userId.value)
                    }
            }
        }.await()
    }

    override suspend fun removeMember(membership: Membership) {
        val documents = queryMemberDataForDeletion(firestore, membership)

        val user = userRepository.getById(membership.userId)

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
                    .document(user.id.value)
                    .let { documentReference ->
                        batch.update(documentReference, FirestoreUser.FIELD_GROUP_ID, newGroup.groupId)
                    }
            }
        }.await()
    }

    private fun createNewGroupAndMembership(user: User): Pair<FirestoreGroup, FirestoreMembership> {
        val inviteCode = InviteCode()

        val group = FirestoreGroup(
            leaderId = user.id.value,
            name = "${user.displayName.value}의 그룹",
            inviteCode = inviteCode.value,
        )

        val membershipId = buildString {
            append(group.groupId)
            append(MEMBERSHIP_ID_SEPARATOR)
            append(user.id.value)
        }

        val membership = FirestoreMembership(
            membershipId,
            group.groupId,
            userId = user.id.value,
            isLeader = true,
            nickname = user.displayName.value,
            profileImage = user.avatar.toFirestoreModel(),
        )

        return group to membership
    }

    companion object {
        private const val MEMBERSHIP_ID_SEPARATOR = '_'
    }
}