package it.stamp.domain.service

import it.stamp.domain.repository.StampRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import javax.inject.Inject
import kotlin.time.Clock

class LeaderboardService @Inject constructor(
    private val stampRepository: StampRepository,
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    fun observeStampCountByMemberForMonth(
        groupId: GroupId,
        yearMonth: YearMonth = clock.todayIn(timeZone).yearMonth
    ): Flow<Map<UserId, Int>> = stampRepository.observeStampCountByMemberForMonth(groupId, yearMonth)
}