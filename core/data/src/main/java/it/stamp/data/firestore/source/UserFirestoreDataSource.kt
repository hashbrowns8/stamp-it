package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.util.usersCollection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore,
) : FirestoreDataSource<FirestoreUser>() {

    override val collection: CollectionReference = firestore.usersCollection

    override val valueType: Class<FirestoreUser> = FirestoreUser::class.java
}