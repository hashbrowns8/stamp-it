package it.stamp.domain.usecase.membership

import it.stamp.domain.exception.GroupJoinException
import it.stamp.domain.repository.GroupRepository
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.MembershipService
import it.stamp.model.membership.InviteCode
import javax.inject.Inject

class JoinGroupWithInviteCodeUseCase @Inject constructor(
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val groupRepository: GroupRepository,
    private val membershipRepository: MembershipRepository,
    private val membershipService: MembershipService,
) {
    suspend operator fun invoke(inviteCode: InviteCode): Result<Unit> =
        runCatching {
            val currentMembership = getMyMembershipUseCase().getOrThrow()

            val targetGroup = groupRepository.findByInviteCode(inviteCode)
                ?: throw GroupJoinException.InvalidInviteCode()

            if (currentMembership.groupId == targetGroup.id) throw GroupJoinException.AlreadyMember()

            val currentGroupMemberCount = membershipRepository.getGroupMemberCount(currentMembership.groupId)

            if (currentGroupMemberCount > 1) {
                throw GroupJoinException.RequiresDataLossConsent(targetGroup.id)
            }

            membershipService.joinGroup(currentMembership, targetGroup.id)
        }
}