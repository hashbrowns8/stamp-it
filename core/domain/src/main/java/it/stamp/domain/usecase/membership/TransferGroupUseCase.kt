package it.stamp.domain.usecase.membership

import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.service.GroupTransferService
import it.stamp.model.membership.Group
import javax.inject.Inject

class TransferGroupUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val groupTransferService: GroupTransferService,
) {
    suspend operator fun invoke(
        leavingGroup: Group,
        joiningGroup: Group,
    ): Result<Group> = runCatching {
        authenticationService.requireUser()
            .let { user ->
                groupTransferService.transfer(user, leavingGroup, joiningGroup)
            }
    }
}