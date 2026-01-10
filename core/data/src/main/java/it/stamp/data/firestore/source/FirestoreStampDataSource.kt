package it.stamp.data.firestore.source

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreStamp
import it.stamp.data.firestore.util.stamps
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.datetime.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreStampDataSource @Inject constructor(
    firestore: FirebaseFirestore,
) : FirestoreDataSource<FirestoreStamp>() {

    override val collection: CollectionReference = firestore.stamps

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
        val snapshot = collection
            .whereEqualTo("groupId", groupId.value)
            .whereEqualTo("month", yearMonth.toString())
            .whereEqualTo("userId", userId.value)
            .count()
            .get(AggregateSource.SERVER)
            .await()

        snapshot.count.toInt()
    }
}