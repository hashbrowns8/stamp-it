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
    override suspend fun getMonthlyGroupStamps(
        groupId: GroupId,
        yearMonth: YearMonth
    ): List<Stamp> = dataSource
        .getMonthlyGroupStamps(groupId, yearMonth)
        .map(StampMapper::toDomainModel)

    override suspend fun getMonthlyMemberStampCount(
        groupId: GroupId,
        userId: UserId,
        yearMonth: YearMonth
    ): Int = dataSource.getMonthlyMemberStampCount(groupId, userId, yearMonth)
}