package it.stamp.domain.usecase

import it.stamp.model.ids.GroupId
import it.stamp.model.membership.InviteCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetInviteCodeUseCase @Inject constructor(
    private val getGroupByIdUseCase: GetGroupByIdUseCase,
) {
    suspend operator fun invoke(groupId: GroupId): Result<InviteCode> =
        getGroupByIdUseCase(groupId).map { it.inviteCode }
}