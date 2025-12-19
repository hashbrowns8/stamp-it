package it.stamp.data.firestore.util

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.userDocument(id: String): DocumentReference =
    collection("users").document(id)

fun FirebaseFirestore.groupDocument(id: String): DocumentReference =
    collection("groups").document(id)

fun FirebaseFirestore.membershipDocument(id: String): DocumentReference =
    collection("memberships").document(id)