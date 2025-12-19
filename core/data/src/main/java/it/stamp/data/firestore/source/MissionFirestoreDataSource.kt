package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreMission
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MissionFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore
) : FirestoreDataSource<FirestoreMission>(FirestoreMission::class.java) {

    override val collection: CollectionReference = firestore.collection(COLLECTION_PATH)

    companion object Companion {
        private const val COLLECTION_PATH = "missions"
    }
}