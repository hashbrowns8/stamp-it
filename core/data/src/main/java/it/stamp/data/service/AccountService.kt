package it.stamp.data.service

import android.R.attr.data
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.mapper.toFirestoreModel
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.queryMemberDataForDeletion
import it.stamp.data.firestore.util.usersCollection
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.AccountService
import it.stamp.model.ids.UserId
import it.stamp.model.membership.InviteCode
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreAccountService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val membershipRepository: MembershipRepository,
) : AccountService {

    override suspend fun registerAccount(userId: UserId): User {
        val displayName = DisplayName()

        val inviteCode = InviteCode()

        val avatar = Avatar.CHARACTER_1.toFirestoreModel()

        val group = FirestoreGroup(
            leaderId = userId.value,
            name = "${displayName.value}의 그룹",
            inviteCode = inviteCode.value,
        )

        val user = FirestoreUser(
            userId = userId.value,
            groupId = group.groupId,
            nickname = displayName.value,
            profileImage = avatar,
        )

        val membershipId = buildString {
            append(group.groupId)
            append(FirestoreMembership.MEMBERSHIP_ID_SEPARATOR)
            append(userId.value)
        }

        val membership = FirestoreMembership(
            membershipId = membershipId,
            groupId = group.groupId,
            userId = userId.value,
            isLeader = true,
            nickname = displayName.value,
            profileImage = avatar,
        )

        firestore.run {
            runBatch { batch ->
                usersCollection.document(userId.value)
                    .let { documentReference ->
                        batch.set(documentReference, user)
                    }

                groupsCollection.document(group.groupId)
                    .let { documentReference ->
                        batch.set(documentReference, data)
                    }

                membershipsCollection.document(membershipId)
                    .let { documentReference ->
                        batch.set(documentReference, membership)
                    }
            }
        }.await()

        return userRepository.getById(userId)
    }

    override suspend fun deleteAccount(userId: UserId) {
        val membership = membershipRepository.getUserMembership(userId)

        val documents = queryMemberDataForDeletion(firestore, membership)

        firestore.run {
            runBatch { batch ->
                documents.forEach { document ->
                    batch.delete(document.reference)
                }

                membershipsCollection
                    .document(membership.id.value)
                    .let(batch::delete)

                groupsCollection
                    .document(membership.groupId.value)
                    .let(batch::delete)

                usersCollection
                    .document(userId.value)
                    .let(batch::delete)
            }
        }
    }
}