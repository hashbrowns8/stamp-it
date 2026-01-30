package it.stamp.domain.usecase.membership

import it.stamp.domain.repository.GroupRepository
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.service.GroupTransferService
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode
import javax.inject.Inject

class JoinGroupWithInviteCodeUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val groupRepository: GroupRepository,
    private val membershipRepository: MembershipRepository,
    private val groupTransferService: GroupTransferService,
) {
    suspend operator fun invoke(inviteCode: InviteCode): Result<JoinGroup> =
        runCatching {
            val user = authenticationService.requireUser()

            val joiningGroup = groupRepository.findByInviteCode(inviteCode)
                ?: return@runCatching JoinGroup.InvalidCode

            val membership = membershipRepository.getUserMembership(user.id)

            if (membership.groupId == joiningGroup.id) return@runCatching JoinGroup.AlreadyInGroup

            val leavingGroup = groupRepository.getById(membership.groupId)

            val leavingGroupMemberCount = membershipRepository.getGroupMemberCount(membership.groupId)

            if (leavingGroupMemberCount > 1) {
                return@runCatching JoinGroup.RequiresDataLossConsent(leavingGroup, joiningGroup)
            }

            groupTransferService.transfer(user, leavingGroup, joiningGroup)
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