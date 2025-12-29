package it.stamp.data.service

import it.stamp.domain.repository.StampRepository
import it.stamp.domain.service.LeaderboardService
import it.stamp.domain.service.MemberService
import it.stamp.model.ids.GroupId
import it.stamp.model.stamp.LeaderboardMember
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearMonth
import javax.inject.Inject
import kotlin.time.Clock

class FirestoreLeaderboardService @Inject constructor(
    private val memberService: MemberService,
    private val stampRepository: StampRepository
) : LeaderboardService {
    override suspend fun getGroupLeaderboard(groupId: GroupId): Result<List<LeaderboardMember>> =
        coroutineScope {
            runCatching {
                val members = memberService.getMembersByGroup(groupId)
                    .getOrThrow()

                val yearMonth = Clock.System
                    .todayIn(TimeZone.currentSystemDefault())
                    .yearMonth

                members.map { member ->
                    async {
                        val stampCount = stampRepository
                            .getMonthlyStampCountByMember(groupId, yearMonth, member.id)
                            .getOrDefault(0)

                        LeaderboardMember(
                            member,
                            rank = 0,
                            stamps = stampCount
                        )
                    }
                }.awaitAll()
                    .sortedByDescending { it.stamps }
                    .mapIndexed { index, leaderboardMember ->
                        leaderboardMember.copy(rank = index + 1)
                    }
            }
        }
}