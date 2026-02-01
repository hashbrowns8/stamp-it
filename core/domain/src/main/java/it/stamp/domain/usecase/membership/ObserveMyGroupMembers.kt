package it.stamp.domain.usecase.membership

import it.stamp.domain.service.CurrentUserContextService
import it.stamp.model.membership.Member
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMyGroupMembers @Inject constructor(
    private val currentUserContextService: CurrentUserContextService,
) {
    operator fun invoke(): Flow<List<Member>> = currentUserContextService.observeMembers()
}