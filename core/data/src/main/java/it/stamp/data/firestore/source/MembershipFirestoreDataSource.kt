package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MembershipFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore
) : FirestoreDataSource<FirestoreMembership>(FirestoreMembership::class.java) {

    override val collection: CollectionReference = firestore.collection(COLLECTION_PATH)

    fun observeGroupMemberships(groupId: GroupId): Flow<List<FirestoreMembership>> =
        collection.whereEqualTo("groupId", groupId.value)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(FirestoreMembership::class.java)
            }
            .flowOn(dispatcher)

    suspend fun getGroupMemberships(groupId: GroupId): List<FirestoreMembership> = withContext(dispatcher) {
        collection.whereEqualTo("groupId", groupId.value)
            .get()
            .await()
            .toObjects(FirestoreMembership::class.java)
    }

    fun observeUserMembership(userId: UserId): Flow<FirestoreMembership> =
        collection.whereEqualTo("userId", userId.value)
            .snapshots()
            .map { snapshot ->
                snapshot
                    .toObjects(FirestoreMembership::class.java)
                    .single()
            }
            .flowOn(dispatcher)

    suspend fun getUserMembership(userId: UserId): FirestoreMembership = withContext(dispatcher) {
        collection.whereEqualTo("userId", userId.value)
            .get()
            .await()
            .toObjects(FirestoreMembership::class.java)
            .single()
    }

    companion object Companion {
        private const val COLLECTION_PATH = "memberships"
    }
}