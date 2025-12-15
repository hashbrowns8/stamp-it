package it.stamp.data.bootstrap

import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.model.toFirestoreGroup
import it.stamp.data.model.toFirestoreMembership
import it.stamp.data.model.toFirestoreUser
import it.stamp.data.util.groupDocument
import it.stamp.data.util.membershipDocument
import it.stamp.data.util.userDocument
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
                set(firestore.userDocument(user.id.value), user.toFirestoreUser(group.id))

                set(firestore.groupDocument(group.id.value), group.toFirestoreGroup(user.id))

                set(firestore.membershipDocument(membership.id.value), membership.toFirestoreMembership(user.displayName, user.avatar))
            }
        }.await()

        return@runCatching user
    }
}