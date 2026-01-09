package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore
) : FirestoreDataSource<FirestoreGroup>(FirestoreGroup::class.java) {

    override val collection: CollectionReference = firestore.collection(COLLECTION_PATH)

    suspend fun getGroupByInviteCode(inviteCode: InviteCode): FirestoreGroup? =
        withContext(dispatcher) {
            collection.whereEqualTo("inviteCode", inviteCode.value)
                .get()
                .await()
                .toObjects(FirestoreGroup::class.java)
                .singleOrNull()
        }

    companion object Companion {
        private const val COLLECTION_PATH = "groups"
    }
}