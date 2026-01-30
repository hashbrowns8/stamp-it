package it.stamp.domain.usecase.membership

import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.AuthenticationService
import it.stamp.model.membership.Membership
import javax.inject.Inject

class GetMyMembershipUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val membershipRepository: MembershipRepository,
) {
    suspend operator fun invoke(): Result<Membership> =
        runCatching {
            authenticationService.requireUser()
                .let { user ->
                    membershipRepository.getUserMembership(user.id)
                }
        }
}