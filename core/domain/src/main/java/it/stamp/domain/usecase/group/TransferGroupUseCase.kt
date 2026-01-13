package it.stamp.domain.usecase.group

import it.stamp.domain.exception.NotAuthenticatedException
import it.stamp.domain.service.AuthService
import it.stamp.domain.service.GroupTransferService
import it.stamp.model.membership.Group
import javax.inject.Inject

class TransferGroupUseCase @Inject constructor(
    private val authService: AuthService,
    private val groupTransferService: GroupTransferService,
) {
    suspend operator fun invoke(
        leavingGroup: Group,
        joiningGroup: Group,
    ): Result<Group> = runCatching {
        authService.currentUser
            ?.let { user ->
                groupTransferService.transferGroup(user, leavingGroup, joiningGroup)
            }
            ?: throw NotAuthenticatedException()
    }
}