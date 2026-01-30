package it.stamp.data.firestore.source

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import it.stamp.common.di.IODispatcher
import it.stamp.data.firestore.model.FirestoreStamp
import it.stamp.data.firestore.util.stampsCollection
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.datetime.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StampFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
) : FirestoreDataSource<FirestoreStamp>(coroutineDispatcher) {

    override val collection: CollectionReference = firestore.stampsCollection

    override val valueType: Class<FirestoreStamp> = FirestoreStamp::class.java

    suspend fun getMonthlyGroupStamps(
        groupId: GroupId,
        yearMonth: YearMonth,
    ): List<FirestoreStamp> = withContext(coroutineDispatcher) {
        val snapshot = collection
            .whereEqualTo("groupId", groupId.value)
            .whereEqualTo("month", yearMonth.toString())
            .get()
            .await()

        snapshot.toObjects(FirestoreStamp::class.java)
    }

    suspend fun getMonthlyMemberStampCount(
        groupId: GroupId,
        yearMonth: YearMonth,
        userId: UserId
    ): Int = withContext(coroutineDispatcher) {
        collection
            .whereEqualTo("groupId", groupId.value)
            .whereEqualTo("month", yearMonth.toString())
            .whereEqualTo("userId", userId.value)
            .count()
            .get(AggregateSource.SERVER)
            .await()
            .count.toInt()
    }

    fun observeMonthlyStampCountByUser(
        groupId: GroupId,
        yearMonth: YearMonth,
    ): Flow<Map<UserId, Int>> = collection
        .whereEqualTo("groupId", groupId.value)
        .whereEqualTo("month", yearMonth.toString())
        .snapshots()
        .map { snapshot ->
            buildMap {
                for (document in snapshot.documents) {
                    val key = document.getString("userId")
                        ?.let(::UserId)
                        ?: continue

                    put(key, getOrDefault(key, 0) + 1)
                }
            }
        }
        .distinctUntilChanged()

    suspend fun deleteUserStampsInGroup(groupId: GroupId, userId: UserId) {
        withContext(coroutineDispatcher) {
            val snapshot = collection
                .whereEqualTo("groupId", groupId.value)
                .whereEqualTo("userId", userId.value)
                .get()
                .await()

            snapshot.documents
                .map { document ->
                    async {
                        document.reference.delete()
                    }
                }
                .awaitAll()
        }
    }
}