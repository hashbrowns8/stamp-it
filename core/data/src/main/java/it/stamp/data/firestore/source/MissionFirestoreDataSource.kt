package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.snapshots
import it.stamp.common.di.IODispatcher
import it.stamp.data.firestore.model.FirestoreMission
import it.stamp.data.firestore.util.missionsCollection
import it.stamp.data.firestore.util.toTimestamp
import it.stamp.domain.exception.MissionNotFoundException
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.MissionStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
    firestore: FirebaseFirestore,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val clock: Clock,
    private val timeZone: TimeZone,
) : FirestoreDataSource<FirestoreMission>(coroutineDispatcher) {

    override val collection: CollectionReference = firestore.missionsCollection

    override val valueType: Class<FirestoreMission> = FirestoreMission::class.java

    fun observeMissionsByAssigner(
        groupId: GroupId,
        assignerId: UserId
    ): Flow<List<FirestoreMission>> =
        collection
            .whereEqualTo("groupId", groupId.value)
            .whereEqualTo("assignedBy", assignerId.value)
            .orderBy("createDate", Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(FirestoreMission::class.java)
            }
            .flowOn(coroutineDispatcher)

    fun observeMissionsByAssigneeThisWeek(
        groupId: GroupId,
        assigneeId: UserId
    ): Flow<List<FirestoreMission>> {
        val today = clock.todayIn(timeZone)

        val after7Days = today.plus(7, DateTimeUnit.DAY)

        return collection
            .whereEqualTo("groupId", groupId.value)
            .whereEqualTo("assignedTo", assigneeId.value)
            .whereLessThanOrEqualTo("dueDate", after7Days.toTimestamp())
            .whereGreaterThanOrEqualTo("dueDate", today.toTimestamp())
            .orderBy("dueDate")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(FirestoreMission::class.java)
            }
            .flowOn(coroutineDispatcher)
    }

    suspend fun updateMissionStatus(
        missionId: MissionId,
        status: MissionStatus,
    ): FirestoreMission = withContext(coroutineDispatcher) {
        collection.document(missionId.value)
            .update("status", status.name.lowercase())
            .await()

        collection.document(missionId.value)
            .get()
            .await()
            .toObject(FirestoreMission::class.java)
            ?: throw MissionNotFoundException()
    }
}