package it.stamp.data.firestore.mapper

import com.google.firebase.Timestamp
import kotlin.time.Instant

fun Timestamp.toKotlinInstant() = Instant.fromEpochSeconds(seconds, nanoseconds.toLong())