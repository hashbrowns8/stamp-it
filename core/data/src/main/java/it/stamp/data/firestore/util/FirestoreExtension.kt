package it.stamp.data.firestore.util

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun FirebaseFirestore.userDocument(id: String): DocumentReference =
    collection("users").document(id)

fun FirebaseFirestore.groupDocument(id: String): DocumentReference =
    collection("groups").document(id)

fun FirebaseFirestore.membershipDocument(id: String): DocumentReference =
    collection("memberships").document(id)

fun Timestamp.toKotlinInstant(): Instant =
    Instant.fromEpochSeconds(seconds, nanoseconds.toLong())

fun Timestamp.toLocalDate(): LocalDate =
    toKotlinInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date