package it.stamp.model.stamp

import it.stamp.model.ids.MissionId
import it.stamp.model.ids.StampId
import kotlin.time.Instant

data class Stamp(
    val id: StampId,
    val missionId: MissionId,
    val type: StampType,
    val createdAt: Instant,
)