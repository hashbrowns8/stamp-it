package it.stamp.data.firestore.source

import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import it.stamp.data.firestore.model.FirestoreMission
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MissionFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore
) : FirestoreDataSource<FirestoreMission>(FirestoreMission::class.java) {

    override val collection: CollectionReference = firestore.collection(COLLECTION_PATH)

    suspend fun getMissionsByAssigner(assignerId: UserId, groupId: GroupId): List<FirestoreMission> =
        withContext(dispatcher) {
            collection
                .whereEqualTo("groupId", groupId.value)
                .whereEqualTo("assignedBy", assignerId.value)
                .orderBy("createDate", Direction.DESCENDING)
                .get()
                .await()
                .toObjects(FirestoreMission::class.java)
        }

    private fun LocalDate.toTimestamp(): Timestamp = atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .let(::Timestamp)

    suspend fun getMissionsByAssigneeThisWeek(assigneeId: UserId, groupId: GroupId): List<FirestoreMission> =
        withContext(dispatcher) {
            val today = LocalDate.now(ZoneId.systemDefault())

            val after7Days = today.plusDays(7)

            collection
                .whereEqualTo("groupId", groupId.value)
                .whereEqualTo("assignedTo", assigneeId.value)
                .whereLessThanOrEqualTo("dueDate", after7Days.toTimestamp())
                .whereGreaterThanOrEqualTo("dueDate", today.toTimestamp())
                .orderBy("dueDate")
                .get()
                .await()
                .toObjects(FirestoreMission::class.java)
                .also {
                    Timber.d("MyMissionsThisWeek(${it.size})")
                }
        }

    companion object Companion {
        private const val COLLECTION_PATH = "missions"
    }
}