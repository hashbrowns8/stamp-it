package it.stamp.domain.usecase.group

import it.stamp.model.membership.InviteCode
import javax.inject.Inject

class GetInviteCodeUseCase @Inject constructor(
    private val getMyGroupUseCase: GetMyGroupUseCase,
) {
    suspend operator fun invoke(): Result<InviteCode> =
        runCatching {
            getMyGroupUseCase()
                .getOrThrow()
                .inviteCode
        }
}