package it.stamp.data.firestore.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.model.FirestoreNotification
import it.stamp.data.firestore.util.notificationsCollection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationFirestoreDataSource @Inject constructor(
    firestore: FirebaseFirestore,
) : FirestoreDataSource<FirestoreNotification>() {

    override val collection: CollectionReference = firestore.notificationsCollection

    override val valueType: Class<FirestoreNotification> = FirestoreNotification::class.java
}