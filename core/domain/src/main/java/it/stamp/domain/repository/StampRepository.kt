package it.stamp.domain.repository

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.stamp.Stamp
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.YearMonth

interface StampRepository {

    suspend fun getStampsForMonth(groupId: GroupId, yearMonth: YearMonth): List<Stamp>

    suspend fun countMemberStampsForMonth(groupId: GroupId, yearMonth: YearMonth, userId: UserId): Int

    fun observeStampCountByMemberForMonth(
        groupId: GroupId,
        yearMonth: YearMonth,
    ): Flow<Map<UserId, Int>>

    suspend fun deleteUserStampsInGroup(groupId: GroupId, userId: UserId)
}