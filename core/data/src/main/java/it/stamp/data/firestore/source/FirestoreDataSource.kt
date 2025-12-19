package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

abstract class FirestoreDataSource<T : Any>(
    private val valueType: Class<T>,
    protected val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    abstract val collection: CollectionReference

    suspend fun create(id: String, data: T) = withContext(dispatcher) {
        collection.document(id)
            .set(data)
            .await()
    }

    suspend fun read(id: String): T? = withContext(dispatcher) {
        collection.document(id)
            .get()
            .await()
            .toObject(valueType)
    }

    suspend fun update(id: String, data: T) = withContext(dispatcher) {
        collection.document(id)
            .set(data, SetOptions.merge())
            .await()
    }

    suspend fun delete(id: String) = withContext(dispatcher) {
        collection.document(id)
            .delete()
            .await()
    }

    fun observe(id: String): Flow<T?> = collection.document(id)
        .snapshots()
        .map { snapshot ->
            snapshot.toObject(valueType)
        }
        .flowOn(dispatcher)
}