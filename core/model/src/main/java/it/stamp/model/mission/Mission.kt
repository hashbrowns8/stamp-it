package it.stamp.model.mission

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Instant

data class Mission(
    val id: MissionId,
    val groupId: GroupId,
    val category: MissionCategory,
    val title: String,
    val assignee: UserId,
    val assigner: UserId,
    val dueDate: LocalDate,
    val status: MissionStatus,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
) {
    val isOverdue: Boolean
        get() = !isCompleted && Clock.System.todayIn(TimeZone.currentSystemDefault()) > dueDate

    val remainingDays: Int
        get() = Clock.System.todayIn(TimeZone.currentSystemDefault()).daysUntil(dueDate)

    val isCompleted: Boolean
        get() = status == MissionStatus.COMPLETED

    fun assign(): Mission = copy(status = MissionStatus.ASSIGNED)

    fun complete(): Mission = copy(status = MissionStatus.COMPLETED)

    fun fail(): Mission = copy(status = MissionStatus.FAILED)
}