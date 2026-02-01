package it.stamp.domain.usecase.membership

import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.MembershipService
import javax.inject.Inject

class LeaveGroupUseCase @Inject constructor(
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val membershipRepository: MembershipRepository,
    private val membershipService: MembershipService,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val membership = getMyMembershipUseCase().getOrThrow()

        val memberCount = membershipRepository.getGroupMemberCount(membership.groupId)

        if (memberCount == 1) { // 떠나지 못해
            return@runCatching
        }

        membershipService.leaveGroup(membership)
    }
}