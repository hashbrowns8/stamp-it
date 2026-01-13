package it.stamp.domain.usecase.membership

import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.domain.exception.NotAuthenticatedException
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.service.AuthService
import it.stamp.domain.service.MemberService
import it.stamp.model.membership.Member
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMyGroupMembersUseCase @Inject constructor(
    private val authService: AuthService,
    private val membershipRepository: MembershipRepository,
    private val memberService: MemberService,
) {
    suspend operator fun invoke(): Result<List<Member>> =
        runCatching {
            val currentUser = authService.currentUser
                ?: throw NotAuthenticatedException()

            val membership = membershipRepository.getUserMembership(currentUser.id)
                ?: throw MembershipNotFoundException()

            memberService.getGroupMembers(membership.groupId)
        }
}