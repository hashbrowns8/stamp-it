package it.stamp.domain.usecase

import it.stamp.domain.repository.GroupRepository
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateGroupJoinUseCase @Inject constructor(
    private val getCurrentGroupMembersUseCase: GetCurrentGroupMembersUseCase,
    private val getGroupMembersUseCase: GetGroupMembersUseCase,
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke(inviteCode: InviteCode) {
        runCatching {
            val targetGroup = groupRepository.getGroupByInviteCode(inviteCode)
                .getOrElse {
                    return@runCatching GroupJoinValidation.InvalidCode
                }

            val targetGroupMembers = getGroupMembersUseCase(targetGroup.id)
                .getOrThrow()

            if (targetGroupMembers.size >= 10) return@runCatching GroupJoinValidation.GroupFull

            val members = getCurrentGroupMembersUseCase().getOrThrow()

            if (members.size > 1) {
                GroupJoinValidation.RequiresConfirmation(targetGroup)
            } else {
                GroupJoinValidation.Allowed(targetGroup)
            }
        }.getOrElse { throwable ->
            GroupJoinValidation.Failure(throwable)
        }
    }
}

sealed interface GroupJoinValidation {
    data class Allowed(val targetGroup: Group) : GroupJoinValidation
    data class RequiresConfirmation(val targetGroup: Group) : GroupJoinValidation
    data object GroupFull : GroupJoinValidation
    data object InvalidCode : GroupJoinValidation
    data class Failure(val throwable: Throwable) : GroupJoinValidation
}