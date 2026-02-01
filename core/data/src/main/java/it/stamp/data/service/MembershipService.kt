package it.stamp.data.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.util.assignNewLeader
import it.stamp.data.firestore.util.createNewGroupAndMembership
import it.stamp.data.firestore.util.createNewMembership
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.queryMemberDataForDeletion
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.MembershipService
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Membership
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreMembershipService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val membershipRepository: MembershipRepository,
) : MembershipService {

    override suspend fun joinGroup(
        currentMembership: Membership,
        targetGroupId: GroupId,
    ) {
        val (isSoloGroup, nextLeaderMembership) = transferContext(currentMembership)

        val documents = queryMemberDataForDeletion(firestore, currentMembership)

        val newMembership = createNewMembership(
            firestore,
            currentMembershipId = currentMembership.id,
            userId = currentMembership.userId.value,
            targetGroupId = targetGroupId.value,
        )

        executeGroupTransferBatch(
            currentMembership,
            isSoloGroup,
            nextLeaderMembership,
            documents,
            newMembership,
        )
    }

    data class TransferGroupContext(
        val isSoloGroup: Boolean,
        val nextLeaderMembership: Membership?,
    )

    private suspend fun transferContext(currentMembership: Membership): TransferGroupContext {
        val groupMemberships = membershipRepository.getGroupMemberships(currentMembership.groupId)

        val isSoloGroup = groupMemberships.size == 1

        val nextLeaderMembership = if (currentMembership.isLeader && !isSoloGroup) {
            groupMemberships
                .filterNot(Membership::isLeader)
                .minByOrNull { it.joinedAt }
        } else {
            null
        }

        return TransferGroupContext(isSoloGroup, nextLeaderMembership)
    }

    private suspend fun executeGroupTransferBatch(
        currentMembership: Membership,
        isSoloGroup: Boolean,
        nextLeaderMembership: Membership?,
        documents: List<DocumentSnapshot>,
        newMembership: FirestoreMembership,
    ) {
        firestore.run {
            runBatch { batch ->
                // 멤버 데이터 삭제
                documents.forEach { document ->
                    batch.delete(document.reference)
                }

                // 기존 멤버십 삭제
                membershipsCollection
                    .document(currentMembership.id.value)
                    .let(batch::delete)

                // 솔로 그룹이면 그룹 삭제
                if (isSoloGroup) {
                    groupsCollection
                        .document(currentMembership.groupId.value)
                        .let(batch::delete)
                }

                // 새 리더 할당
                if (nextLeaderMembership != null) batch.assignNewLeader(firestore, nextLeaderMembership)

                // 새 멤버십 생성
                membershipsCollection
                    .document(newMembership.membershipId)
                    .let { documentReference -> batch.set(documentReference, newMembership) }
            }
        }.await()
    }

    override suspend fun leaveGroup(currentMembership: Membership) {
        val nextLeaderMembership = if (currentMembership.isLeader) {
            val groupMemberships = membershipRepository.getGroupMemberships(currentMembership.groupId)

            groupMemberships
                .filterNot(Membership::isLeader)
                .minByOrNull { it.joinedAt }
        } else {
            null
        }

        val documents = queryMemberDataForDeletion(firestore, currentMembership)

        val (newGroup, newMembership) = createNewGroupAndMembership(
            firestore,
            currentMembership.id,
            currentMembership.userId,
        )

        executeGroupLeaveBatch(
            currentMembership = currentMembership,
            nextLeaderMembership,
            documents,
            newGroup,
            newMembership,
        )
    }

    private suspend fun executeGroupLeaveBatch(
        currentMembership: Membership,
        nextLeaderMembership: Membership?,
        documents: List<DocumentSnapshot>,
        newGroup: FirestoreGroup,
        newMembership: FirestoreMembership,
    ) {
        firestore.run {
            runBatch { batch ->
                // 멤버 데이터 삭제
                documents.forEach { document ->
                    batch.delete(document.reference)
                }

                // 기존 멤버십 삭제
                membershipsCollection
                    .document(currentMembership.id.value)
                    .let(batch::delete)

                // 새 리더 할당
                if (nextLeaderMembership != null) batch.assignNewLeader(firestore, nextLeaderMembership)

                // 새 그룹 생성
                batch.set(groupsCollection.document(newGroup.groupId), newGroup)

                // 새 멤버십 생성
                batch.set(membershipsCollection.document(newMembership.membershipId), newMembership)
            }
        }.await()
    }
}