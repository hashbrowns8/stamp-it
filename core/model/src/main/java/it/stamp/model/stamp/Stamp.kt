package it.stamp.model.stamp

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.StampId
import it.stamp.model.ids.UserId

data class Stamp(
    val id: StampId,
    val groupId: GroupId,
    val userId: UserId,
)