package it.stamp.data.firestore.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

val FirebaseFirestore.groups: CollectionReference
    get() = collection("groups")

val FirebaseFirestore.memberships: CollectionReference
    get() = collection("memberships")

val FirebaseFirestore.missions: CollectionReference
    get() = collection("missions")

val FirebaseFirestore.notifications: CollectionReference
    get() = collection("DataNoticeFirestore")

val FirebaseFirestore.stamps: CollectionReference
    get() = collection("stamps")

val FirebaseFirestore.users: CollectionReference
    get() = collection("users")
