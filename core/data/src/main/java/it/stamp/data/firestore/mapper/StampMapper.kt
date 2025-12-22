package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreStamp
import it.stamp.data.firestore.util.toKotlinInstant
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.StampId
import it.stamp.model.stamp.Stamp
import it.stamp.model.stamp.StampType

object StampMapper {
    fun toDomainModel(stamp: FirestoreStamp) = with(stamp) {
        val type = type.removePrefix("stamp")
            .replaceFirstChar { it.uppercaseChar() }
            .let(StampType::valueOf)

        Stamp(
            id = StampId(stampId),
            missionId = MissionId(missionId),
            type = type,
            createdAt = createdAt.toKotlinInstant()
        )
    }
}