package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreStamp
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.StampId
import it.stamp.model.stamp.Stamp
import it.stamp.model.stamp.StampType

fun FirestoreStamp.toDomainModel(): Stamp {
    val stampType = parseStampType(type)

    return Stamp(
        id = StampId(stampId),
        missionId = MissionId(missionId),
        type = stampType,
        createdAt = createdAt.toKotlinInstant(),
    )
}

private fun parseStampType(value: String): StampType {
    return value.removePrefix("stamp")
        .uppercase()
        .let(StampType::valueOf)
}