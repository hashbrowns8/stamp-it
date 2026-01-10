package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import it.stamp.data.firestore.model.FirestoreModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

abstract class FirestoreDataSource<T : FirestoreModel>(
    protected val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    abstract val collection: CollectionReference

    abstract val valueType: Class<T>

    suspend fun create(id: String, data: T) {
        withContext(coroutineDispatcher) {
            collection.document(id)
                .set(data)
                .await()
        }
    }

    suspend fun read(id: String): T? = withContext(coroutineDispatcher) {
        collection.document(id)
            .get()
            .await()
            .toObject(valueType)
    }

    suspend fun update(id: String, data: T) {
        withContext(coroutineDispatcher) {
            collection.document(id)
                .set(data, SetOptions.merge())
                .await()
        }
    }

    suspend fun delete(id: String) {
        withContext(coroutineDispatcher) {
            collection.document(id)
                .delete()
                .await()
        }
    }

    fun observe(id: String): Flow<T?> = collection.document(id)
        .snapshots()
        .map { snapshot ->
            snapshot.toObject(valueType)
        }
        .flowOn(coroutineDispatcher)
}