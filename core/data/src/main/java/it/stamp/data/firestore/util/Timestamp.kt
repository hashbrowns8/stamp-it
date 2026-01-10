package it.stamp.data.firestore.util

import com.google.firebase.Timestamp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

fun Timestamp.toKotlinInstant(): Instant =
    Instant.fromEpochSeconds(seconds, nanoseconds.toLong())

fun Timestamp.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
    toKotlinInstant()
        .toLocalDateTime(timeZone)
        .date

fun Instant.toTimestamp(): Timestamp = toJavaInstant().let(::Timestamp)

fun LocalDate.toTimestamp(
    time: LocalTime = LocalTime(0, 0),
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): Timestamp = LocalDateTime(this, time)
    .toInstant(timeZone)
    .toJavaInstant()
    .let(::Timestamp)