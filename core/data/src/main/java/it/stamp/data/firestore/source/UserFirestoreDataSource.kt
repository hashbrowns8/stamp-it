package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore
) : FirestoreDataSource<FirestoreUser>(FirestoreUser::class.java) {

    override val collection: CollectionReference = firestore.collection(COLLECTION_PATH)

    companion object Companion {
        private const val COLLECTION_PATH = "users"
    }
}