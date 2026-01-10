package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreStamp
import it.stamp.data.firestore.util.toKotlinInstant
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.StampId
import it.stamp.model.stamp.Stamp
import it.stamp.model.stamp.StampType

object StampMapper {
    fun toDomainModel(stamp: FirestoreStamp) = with(stamp) {
        val type = parseType(type)

        Stamp(
            id = StampId(id),
            missionId = MissionId(missionId),
            type = type,
            createdAt = createdAt.toKotlinInstant()
        )
    }

    private fun parseType(type: String): StampType =
        type.removePrefix("stamp")
            .uppercase()
            .let(StampType::valueOf)
}