package it.stamp.model.mission

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlin.time.Clock
import kotlin.time.Instant

data class Mission(
    val id: MissionId,
    val groupId: GroupId,
    val category: MissionCategory,
    val title: String,
    val assignee: UserId,
    val assigner: UserId,
    val dueDate: Instant,
    val status: MissionStatus,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
) {
    fun isOverdue(clock: Clock = Clock.System): Boolean = !isDone && clock.now() > dueDate

    fun daysUntilDue(
        clock: Clock = Clock.System,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): Int = clock.now().daysUntil(dueDate, timeZone)

    val isDone: Boolean
        get() = status == MissionStatus.DONE

    fun assign(): Mission = copy(status = MissionStatus.ASSIGNED)

    fun complete(): Mission = copy(status = MissionStatus.DONE)

    fun fail(): Mission = copy(status = MissionStatus.FAILED)
}