package it.stamp.domain.usecase.group

import it.stamp.domain.repository.GroupRepository
import it.stamp.domain.service.AuthService
import it.stamp.domain.service.GroupTransferService
import it.stamp.domain.service.MemberService
import it.stamp.domain.usecase.membership.GetMyMembershipUseCase
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode
import javax.inject.Inject

class JoinGroupWithInviteCodeUseCase @Inject constructor(
    private val authService: AuthService,
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val groupRepository: GroupRepository,
    private val memberService: MemberService,
    private val groupTransferService: GroupTransferService,
) {
    suspend operator fun invoke(inviteCode: InviteCode): Result<JoinGroup> =
        runCatching {
            val user = authService.requireCurrentUser()

            val joiningGroup = groupRepository.findByInviteCode(inviteCode)
                ?: return@runCatching JoinGroup.InvalidCode

            val membership = getMyMembershipUseCase().getOrThrow()

            if (membership.groupId == joiningGroup.id) return@runCatching JoinGroup.AlreadyInGroup

            val leavingGroup = groupRepository.getById(membership.groupId)

            val leavingGroupMemberCount = memberService.getGroupMemberCount(leavingGroup.id)

            if (leavingGroupMemberCount > 1) {
                return@runCatching JoinGroup.RequiresDataLossConsent(leavingGroup, joiningGroup)
            }

            groupTransferService.transferGroup(user, leavingGroup, joiningGroup)
                .let(JoinGroup::Success)
        }
}

sealed interface JoinGroup {
    data object InvalidCode : JoinGroup

    data object AlreadyInGroup : JoinGroup

    data class RequiresDataLossConsent(
        val leavingGroup: Group,
        val joiningGroup: Group,
    ) : JoinGroup

    data class Success(val group: Group) : JoinGroup
}