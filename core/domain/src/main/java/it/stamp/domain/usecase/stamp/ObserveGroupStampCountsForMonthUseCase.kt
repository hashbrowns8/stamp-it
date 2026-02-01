package it.stamp.domain.usecase.stamp

import it.stamp.domain.repository.StampRepository
import it.stamp.domain.usecase.group.ObserveMyGroupUseCase
import it.stamp.model.ids.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import javax.inject.Inject
import kotlin.time.Clock

class ObserveGroupStampCountsForMonthUseCase @Inject constructor(
    private val observeMyGroupUseCase: ObserveMyGroupUseCase,
    private val stampRepository: StampRepository,
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    operator fun invoke(
        yearMonth: YearMonth = clock.todayIn(timeZone).yearMonth
    ): Flow<Map<UserId, Int>> = observeMyGroupUseCase()
        .map { group -> group?.id }
        .distinctUntilChanged()
        .flatMapLatest { groupId ->
            if (groupId == null) {
                flowOf(emptyMap())
            } else {
                stampRepository.observeGroupStampCountsForMonth(groupId, yearMonth)
            }
        }
}