package it.stamp.domain.usecase.group

import it.stamp.domain.service.CurrentUserContextService
import it.stamp.model.membership.Group
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMyGroupUseCase @Inject constructor(
    private val currentUserContextService: CurrentUserContextService,
) {
    operator fun invoke(): Flow<Group?> = currentUserContextService.observeGroup()
}