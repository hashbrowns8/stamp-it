package it.stamp.data.repository

import it.stamp.data.firestore.mapper.StampMapper
import it.stamp.data.firestore.source.StampFirestoreDataSource
import it.stamp.domain.repository.StampRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.stamp.Stamp
import kotlinx.datetime.YearMonth
import javax.inject.Inject

class StampDataRepository @Inject constructor(
    private val dataSource: StampFirestoreDataSource
) : StampRepository {
    override suspend fun getMonthlyStampsByGroup(
        groupId: GroupId,
        yearMonth: YearMonth
    ): Result<List<Stamp>> = runCatching {
        dataSource
            .getMonthlyGroupStamps(groupId, yearMonth)
            .map(StampMapper::toDomainModel)
    }

    override suspend fun getMonthlyStampCountByMember(
        groupId: GroupId,
        yearMonth: YearMonth,
        userId: UserId
    ): Result<Int> = runCatching {
        dataSource.getMonthlyMemberStampCount(groupId, yearMonth, userId)
    }
}