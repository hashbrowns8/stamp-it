package it.stamp.domain.usecase.membership

import it.stamp.domain.service.CurrentUserContextService
import it.stamp.model.membership.Membership
import javax.inject.Inject

class GetMyMembershipUseCase @Inject constructor(
    private val currentUserContextService: CurrentUserContextService
) {
    suspend operator fun invoke(): Result<Membership> =
        runCatching {
            currentUserContextService.requireMembership()
        }
}