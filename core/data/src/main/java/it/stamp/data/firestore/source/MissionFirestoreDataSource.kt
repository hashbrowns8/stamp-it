package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import it.stamp.data.firestore.model.FirestoreMission
import it.stamp.data.firestore.util.toTimestamp
import it.stamp.domain.exception.MissionNotFoundException
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.MissionStatus
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Clock

@Singleton
class MissionFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore
) : FirestoreDataSource<FirestoreMission>(FirestoreMission::class.java) {

    override val collection: CollectionReference = firestore.collection(COLLECTION_PATH)

    suspend fun getMissionsByAssigner(
        assignerId: UserId,
        groupId: GroupId
    ): List<FirestoreMission> = withContext(dispatcher) {
        collection
            .whereEqualTo("groupId", groupId.value)
            .whereEqualTo("assignedBy", assignerId.value)
            .orderBy("createDate", Direction.DESCENDING)
            .get()
            .await()
            .toObjects(FirestoreMission::class.java)
    }

    suspend fun getMissionsByAssigneeThisWeek(
        assigneeId: UserId,
        groupId: GroupId
    ): List<FirestoreMission> = withContext(dispatcher) {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val after7Days = today.plus(7, DateTimeUnit.DAY)

        collection
            .whereEqualTo("groupId", groupId.value)
            .whereEqualTo("assignedTo", assigneeId.value)
            .whereLessThanOrEqualTo("dueDate", after7Days.toTimestamp())
            .whereGreaterThanOrEqualTo("dueDate", today.toTimestamp())
            .orderBy("dueDate")
            .get()
            .await()
            .toObjects(FirestoreMission::class.java)
    }

    suspend fun updateMissionStatus(
        missionId: MissionId,
        status: MissionStatus,
    ): FirestoreMission = withContext(dispatcher) {
        collection.document(missionId.value)
            .update("status", status.name.lowercase())
            .await()

        collection.document(missionId.value)
            .get()
            .await()
            .toObject(FirestoreMission::class.java)
            ?: throw MissionNotFoundException()
    }


    companion object Companion {
        private const val COLLECTION_PATH = "missions"
    }
}