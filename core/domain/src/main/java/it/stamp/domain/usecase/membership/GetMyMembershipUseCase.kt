package it.stamp.domain.usecase.membership

import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.domain.exception.NotAuthenticatedException
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.AuthService
import it.stamp.model.membership.Membership
import javax.inject.Inject

class GetMyMembershipUseCase @Inject constructor(
    private val authService: AuthService,
    private val membershipRepository: MembershipRepository,
) {
    suspend operator fun invoke(): Result<Membership> =
        runCatching {
            val currentUser = authService.currentUser
                ?: throw NotAuthenticatedException()

            membershipRepository.getUserMembership(currentUser.id)
                ?: throw MembershipNotFoundException()
        }
}