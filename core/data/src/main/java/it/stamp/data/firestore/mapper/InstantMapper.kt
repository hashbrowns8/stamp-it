package it.stamp.data.firestore.mapper

import com.google.firebase.Timestamp
import kotlin.time.Instant
import kotlin.time.toJavaInstant

fun Timestamp.toKotlinInstant(): Instant =
    Instant.fromEpochSeconds(seconds, nanoseconds.toLong())

fun Instant.toTimestamp(): Timestamp = toJavaInstant().let(::Timestamp)