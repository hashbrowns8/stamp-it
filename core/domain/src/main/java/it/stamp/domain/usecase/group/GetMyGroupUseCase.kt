package it.stamp.domain.usecase.group

import it.stamp.domain.service.CurrentUserContextService
import it.stamp.model.membership.Group
import javax.inject.Inject

class GetMyGroupUseCase @Inject constructor(
    private val currentUserContextService: CurrentUserContextService,
) {
    suspend operator fun invoke(): Result<Group> =
        runCatching {
            currentUserContextService.requireGroup()
        }
}