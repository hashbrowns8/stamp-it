package it.stamp.data.firestore.mapper

import it.stamp.data.firestore.model.FirestoreMission
import it.stamp.data.firestore.util.toKotlinInstant
import it.stamp.data.firestore.util.toLocalDate
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.Mission
import it.stamp.model.mission.MissionCategory
import it.stamp.model.mission.MissionStatus

object MissionMapper {
    fun toDomainModel(mission: FirestoreMission): Mission = with(mission) {
        Mission(
            id = MissionId(missionId),
            groupId = GroupId(groupId),
            category = MissionCategory.valueOf(category.uppercase()),
            title = title,
            assignee = UserId(assignedTo),
            assigner = UserId(assignedBy),
            dueDate = dueDate.toLocalDate(),
            status = MissionStatus.valueOf(status.uppercase()),
            createdAt = createDate.toKotlinInstant(),
        )
    }
}