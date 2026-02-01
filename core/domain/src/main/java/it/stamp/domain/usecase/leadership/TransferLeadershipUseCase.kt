package it.stamp.domain.usecase.leadership

import it.stamp.domain.exception.LeadershipException
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.LeadershipService
import it.stamp.domain.usecase.membership.GetMyMembershipUseCase
import it.stamp.model.ids.UserId
import javax.inject.Inject

class TransferLeadershipUseCase @Inject constructor(
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val membershipRepository: MembershipRepository,
    private val leadershipService: LeadershipService,
) {
    suspend operator fun invoke(memberId: UserId): Result<Unit> =
        runCatching {
            val myMembership = getMyMembershipUseCase().getOrThrow()

            require(myMembership.isLeader) {
                throw LeadershipException.Unauthorized()
            }

            val newLeaderMembership = membershipRepository.getUserMembership(memberId)

            leadershipService.transferLeadership(myMembership, newLeaderMembership)
        }
}