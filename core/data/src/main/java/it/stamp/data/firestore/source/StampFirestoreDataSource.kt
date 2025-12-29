package it.stamp.data.firestore.source

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreStamp
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.datetime.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StampFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore
) : FirestoreDataSource<FirestoreStamp>(FirestoreStamp::class.java) {

    override val collection: CollectionReference = firestore.collection(COLLECTION_PATH)

    suspend fun getMonthlyGroupStamps(
        groupId: GroupId,
        yearMonth: YearMonth,
    ): List<FirestoreStamp> = withContext(dispatcher) {
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
    ): Int = withContext(dispatcher) {
        val snapshot = collection
            .whereEqualTo("groupId", groupId.value)
            .whereEqualTo("month", yearMonth.toString())
            .whereEqualTo("userId", userId.value)
            .count()
            .get(AggregateSource.SERVER)
            .await()

        snapshot.count.toInt()
    }

    companion object Companion {
        private const val COLLECTION_PATH = "stamps"
    }
}