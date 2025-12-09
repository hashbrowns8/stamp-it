package it.stamp.model.stamp

import it.stamp.model.ids.MembershipId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.StampId
import kotlin.time.Instant

data class Stamp(
    val id: StampId,
    val membershipId: MembershipId,
    val missionId: MissionId,
    val color: StampColor,
    val createdAt: Instant,
)