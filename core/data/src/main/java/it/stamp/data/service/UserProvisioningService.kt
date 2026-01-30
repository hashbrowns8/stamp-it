package it.stamp.data.service

import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.mapper.UserMapper
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.usersCollection
import it.stamp.domain.service.UserProvisioningService
import it.stamp.model.ids.UserId
import it.stamp.model.membership.InviteCode
import it.stamp.model.user.DisplayName
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserProvisioningService @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UserProvisioningService {

    override suspend fun provision(userId: UserId): User {
        with(firestore) {
            val displayName = DisplayName()

            val inviteCode = InviteCode()

            val group = FirestoreGroup(
                leaderId = userId.value,
                name = "${displayName.value}의 그룹",
                inviteCode = inviteCode.value,
            )

            val user = FirestoreUser(
                userId = userId.value,
                group.groupId,
                nickname = displayName.value,
            )

            val membershipId = buildString {
                append(group.groupId)
                append('_')
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

            runBatch { batch ->
                with(batch) {
                    set(usersCollection.document(user.userId), user)
                    set(groupsCollection.document(group.groupId), group)
                    set(membershipsCollection.document(membership.membershipId), membership)
                }
            }.await()

            return user.let(UserMapper::toDomainModel)
        }
    }
}