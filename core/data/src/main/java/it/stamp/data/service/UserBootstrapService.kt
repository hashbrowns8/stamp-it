package it.stamp.data.service

import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.groupDocument
import it.stamp.data.firestore.util.membershipDocument
import it.stamp.data.firestore.util.userDocument
import it.stamp.domain.service.UserBootstrapService
import it.stamp.model.membership.Group
import it.stamp.model.membership.Membership
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserBootstrapService @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserBootstrapService {

    override suspend fun bootstrap(
        user: User,
        group: Group,
        membership: Membership
    ): Result<User> = runCatching {
        firestore.runBatch { batch ->
            with(batch) {
                val userFirestore = FirestoreUser(
                    userId = user.id.value,
                    groupId = group.id.value,
                    nickname = user.displayName,
                    profileImage = user.avatar.ifEmpty {
                        DEFAULT_PROFILE_IMAGE
                    },
                )

                set(firestore.userDocument(user.id.value), userFirestore)

                val groupFirestore = FirestoreGroup(
                    groupId = group.id.value,
                    leaderId = user.id.value,
                    name = group.name,
                    inviteCode = group.inviteCode.value,
                )

                set(firestore.groupDocument(group.id.value), groupFirestore)

                val membershipFirestore = FirestoreMembership(
                    membershipId = membership.id.value,
                    groupId = group.id.value,
                    userId = user.id.value,
                    isLeader = membership.isLeader,
                    nickname = user.displayName,
                    profileImage = user.avatar,
                )

                set(firestore.membershipDocument(membership.id.value), membershipFirestore)
            }
        }.await()

        return@runCatching user
    }

    companion object {
        private const val DEFAULT_PROFILE_IMAGE = "profileImage1"
    }
}