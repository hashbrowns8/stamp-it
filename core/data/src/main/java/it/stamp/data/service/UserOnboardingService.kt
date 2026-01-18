package it.stamp.data.service

import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.mapper.UserMapper
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.usersCollection
import it.stamp.domain.service.UserOnboardingService
import it.stamp.model.ids.UserId
import it.stamp.model.membership.InviteCode
import it.stamp.model.user.DisplayName
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserOnboardingService @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UserOnboardingService {

    override suspend fun onboard(id: UserId): User {
        with(firestore) {
            val displayName = DisplayName()

            val inviteCode = InviteCode()

            val group = FirestoreGroup(
                leaderId = id.value,
                name = "${displayName.value}의 그룹",
                inviteCode = inviteCode.value,
            )

            val user = FirestoreUser(
                id.value,
                group.id,
                displayName.value,
            )

            val membershipId = buildString {
                append(group.id)
                append('_')
                append(user.id)
            }

            val membership = FirestoreMembership(
                membershipId,
                group.id,
                user.id,
                isLeader = true,
                user.nickname,
                user.profileImage,
            )

            runBatch { batch ->
                with(batch) {
                    set(usersCollection.document(user.id), user)
                    set(groupsCollection.document(user.id), group)
                    set(membershipsCollection.document(user.id), membership)
                }
            }.await()

            return user.let(UserMapper::toDomainModel)
        }
    }
}