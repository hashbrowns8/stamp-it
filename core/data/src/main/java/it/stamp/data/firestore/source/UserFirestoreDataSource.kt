package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.common.di.IODispatcher
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.usersCollection
import it.stamp.model.ids.UserId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
) : FirestoreDataSource<FirestoreUser>(coroutineDispatcher) {

    override val collection: CollectionReference = firestore.usersCollection

    override val valueType: Class<FirestoreUser> = FirestoreUser::class.java

    suspend fun getByIds(ids: List<UserId>): List<FirestoreUser> {
        return collection
            .whereIn(FieldPath.documentId(), ids.map(UserId::value))
            .get()
            .await()
            .toObjects(valueType)
    }
}