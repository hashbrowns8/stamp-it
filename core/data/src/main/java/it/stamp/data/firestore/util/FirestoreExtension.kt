package it.stamp.data.firestore.util

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

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

fun Instant.toTimestamp(): Timestamp = toJavaInstant().let(::Timestamp)

fun LocalDate.toTimestamp(): Timestamp = LocalDateTime(this, LocalTime(0, 0))
    .toInstant(TimeZone.currentSystemDefault())
    .toJavaInstant()
    .let(::Timestamp)