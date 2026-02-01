package it.stamp.domain.usecase.user

import it.stamp.domain.exception.AccountDeletionException
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.AccountService
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.usecase.membership.GetMyMembershipUseCase
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val membershipRepository: MembershipRepository,
    private val accountService: AccountService,
) {
    suspend operator fun invoke() = runCatching {
        val userId = authenticationService.requireAuthenticated().userId

        val membership = getMyMembershipUseCase().getOrThrow()

        if (membership.isLeader) {
            val memberCount = membershipRepository.getGroupMemberCount(membership.groupId)

            if (memberCount > 1) {
                throw AccountDeletionException.LeadershipTransferRequired()
            } else {
                accountService.deleteAccount(userId)
            }
        } else {
            accountService.deleteAccount(userId)
        }

        authenticationService.signOut()
    }
}