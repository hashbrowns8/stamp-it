package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.util.groups
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreGroupDataSource @Inject constructor(
    firestore: FirebaseFirestore,
) : FirestoreDataSource<FirestoreGroup>() {

    override val collection: CollectionReference = firestore.groups

    override val valueType: Class<FirestoreGroup> = FirestoreGroup::class.java
}