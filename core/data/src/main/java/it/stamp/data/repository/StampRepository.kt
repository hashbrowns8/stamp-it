package it.stamp.data.repository

import it.stamp.data.firestore.mapper.toDomainModel
import it.stamp.data.firestore.source.StampFirestoreDataSource
import it.stamp.domain.repository.StampRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.stamp.Stamp
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.YearMonth
import javax.inject.Inject

class StampDataRepository @Inject constructor(
    private val dataSource: StampFirestoreDataSource
) : StampRepository {

    override suspend fun getStampsForMonth(groupId: GroupId, yearMonth: YearMonth): List<Stamp> =
        dataSource.getStampsByGroupAndMonth(groupId, yearMonth)
            .map { stamp -> stamp.toDomainModel() }

    override suspend fun getMemberStampCountForMonth(
        groupId: GroupId,
        userId: UserId,
        yearMonth: YearMonth
    ): Int = dataSource.getMonthlyMemberStampCount(groupId, yearMonth, userId)

    override fun observeGroupStampCountsForMonth(
        groupId: GroupId,
        yearMonth: YearMonth
    ): Flow<Map<UserId, Int>> = dataSource.observeMonthlyStampCountByUser(groupId, yearMonth)

    override suspend fun deleteUserStampsInGroup(groupId: GroupId, userId: UserId) =
        dataSource.deleteUserStampsInGroup(groupId, userId)
}