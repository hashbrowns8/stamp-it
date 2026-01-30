package it.stamp.data.firestore.source

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import it.stamp.common.di.IODispatcher
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MembershipFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
) : FirestoreDataSource<FirestoreMembership>(coroutineDispatcher) {

    override val collection: CollectionReference = firestore.membershipsCollection

    override val valueType: Class<FirestoreMembership> = FirestoreMembership::class.java

    fun observeGroupMemberships(groupId: GroupId): Flow<List<FirestoreMembership>> =
        collection.whereEqualTo("groupId", groupId.value)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(FirestoreMembership::class.java)
            }
            .flowOn(coroutineDispatcher)

    suspend fun getGroupMemberships(
        groupId: GroupId
    ): List<FirestoreMembership> = withContext(coroutineDispatcher) {
        collection.whereEqualTo("groupId", groupId.value)
            .get()
            .await()
            .toObjects(FirestoreMembership::class.java)
    }

    suspend fun getGroupMemberCount(groupId: GroupId): Int = withContext(coroutineDispatcher) {
        collection.whereEqualTo("groupId", groupId.value)
            .count()
            .get(AggregateSource.SERVER)
            .await()
            .count
            .toInt()
    }

    fun observeUserMembership(userId: UserId): Flow<FirestoreMembership?> =
        collection.whereEqualTo("userId", userId.value)
            .snapshots()
            .map { snapshot ->
                snapshot
                    .toObjects(FirestoreMembership::class.java)
                    .singleOrNull()
            }
            .flowOn(coroutineDispatcher)

    suspend fun findUserMembership(userId: UserId): FirestoreMembership? = withContext(coroutineDispatcher) {
        collection.whereEqualTo("userId", userId.value)
            .get()
            .await()
            .toObjects(FirestoreMembership::class.java)
            .singleOrNull()
    }
}