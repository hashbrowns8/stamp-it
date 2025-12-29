package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.stamp.Stamp
import kotlinx.datetime.YearMonth

interface StampRepository {
    suspend fun getMonthlyStampsByGroup(
        groupId: GroupId,
        yearMonth: YearMonth,
    ): Result<List<Stamp>>

    suspend fun getMonthlyStampCountByMember(
        groupId: GroupId,
        yearMonth: YearMonth,
        userId: UserId,
    ): Result<Int>
}