package it.stamp.data.repository

import it.stamp.data.firestore.mapper.StampMapper
import it.stamp.data.firestore.source.StampFirestoreDataSource
import it.stamp.domain.repository.StampRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.stamp.Stamp
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.YearMonth
import javax.inject.Inject

class StampDataRepository @Inject constructor(
    private val firestoreDataSource: StampFirestoreDataSource
) : StampRepository {

    override suspend fun getStampsForMonth(
        groupId: GroupId,
        yearMonth: YearMonth
    ): List<Stamp> = firestoreDataSource.getMonthlyGroupStamps(groupId, yearMonth)
        .map(StampMapper::toDomainModel)

    override suspend fun countMemberStampsForMonth(
        groupId: GroupId,
        yearMonth: YearMonth,
        userId: UserId
    ): Int = firestoreDataSource.getMonthlyMemberStampCount(groupId, yearMonth, userId)

    override fun observeStampCountByMemberForMonth(
        groupId: GroupId,
        yearMonth: YearMonth
    ): Flow<Map<UserId, Int>> = firestoreDataSource.observeMonthlyStampCountByUser(groupId, yearMonth)

    override suspend fun deleteUserStampsInGroup(groupId: GroupId, userId: UserId) =
        firestoreDataSource.deleteUserStampsInGroup(groupId, userId)
}