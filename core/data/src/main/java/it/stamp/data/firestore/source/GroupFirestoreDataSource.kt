package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.common.di.IODispatcher
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
) : FirestoreDataSource<FirestoreGroup>(coroutineDispatcher) {

    override val collection: CollectionReference = firestore.groupsCollection

    override val valueType: Class<FirestoreGroup> = FirestoreGroup::class.java

    suspend fun findGroupByInviteCode(inviteCode: InviteCode): FirestoreGroup? =
        withContext(coroutineDispatcher) {
            collection.whereEqualTo("inviteCode", inviteCode.value)
                .get()
                .await()
                .toObjects(FirestoreGroup::class.java)
                .singleOrNull()
        }
}