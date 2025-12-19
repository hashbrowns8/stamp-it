package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.stamp.Stamp
import kotlinx.datetime.YearMonth

interface StampRepository {
    suspend fun getMonthlyGroupStamps(groupId: GroupId, yearMonth: YearMonth): List<Stamp>

    suspend fun getMonthlyMemberStampCount(
        groupId: GroupId,
        userId: UserId,
        yearMonth: YearMonth,
    ): Int
}