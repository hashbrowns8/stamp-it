package it.stamp.domain.usecase.membership

import it.stamp.domain.exception.UnauthorizedException
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.MembershipService
import it.stamp.model.ids.UserId
import javax.inject.Inject

class TransferLeadershipUseCase @Inject constructor(
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val membershipRepository: MembershipRepository,
    private val membershipService: MembershipService,
) {
    suspend operator fun invoke(memberId: UserId): Result<Unit> =
        runCatching {
            val myMembership = getMyMembershipUseCase().getOrThrow()

            require(myMembership.isLeader) {
                throw UnauthorizedException()
            }

            val newLeaderMembership = membershipRepository.getUserMembership(memberId)

            membershipService.transferLeadership(myMembership, newLeaderMembership)
        }
}