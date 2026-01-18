package it.stamp.domain.usecase.group

import it.stamp.domain.repository.StampRepository
import it.stamp.domain.service.MemberService
import it.stamp.model.stamp.LeaderboardMember
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import javax.inject.Inject
import kotlin.time.Clock

class GetMyGroupLeaderboardUseCase @Inject constructor(
    private val getMyGroupUseCase: GetMyGroupUseCase,
    private val memberService: MemberService,
    private val stampRepository: StampRepository,
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {
    suspend operator fun invoke(): Result<List<LeaderboardMember>> =
        runCatching {
            val group = getMyGroupUseCase().getOrThrow()

            val members = memberService.getGroupMembers(group.id)

            val yearMonth = clock.todayIn(timeZone).yearMonth

            coroutineScope {
                members.map { member ->
                    async {
                        val stampCount = stampRepository.getMonthlyStampCountByMember(group.id, yearMonth, member.id)

                        LeaderboardMember(
                            member,
                            rank = 0,
                            stamps = stampCount
                        )
                    }
                }.awaitAll()
            }.sortedByDescending { it.stamps }
                .mapIndexed { index, leaderboardMember ->
                    leaderboardMember.copy(rank = index + 1)
                }
        }
}