package it.stamp.data.firestore.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

val FirebaseFirestore.groupsCollection: CollectionReference
    get() = collection("groups")

val FirebaseFirestore.membershipsCollection: CollectionReference
    get() = collection("memberships")

val FirebaseFirestore.missionsCollection: CollectionReference
    get() = collection("missions")

val FirebaseFirestore.notificationsCollection: CollectionReference
    get() = collection("DataNoticeFirestore")

val FirebaseFirestore.stampsCollection: CollectionReference
    get() = collection("stamps")

val FirebaseFirestore.usersCollection: CollectionReference
    get() = collection("users")
