package it.stamp.domain.usecase.membership

import it.stamp.domain.service.CurrentUserContextService
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMyMembershipUseCase @Inject constructor(
    private val currentUserContextService: CurrentUserContextService,
) {
    operator fun invoke(): Flow<Membership?> = currentUserContextService.observeMembership()
}