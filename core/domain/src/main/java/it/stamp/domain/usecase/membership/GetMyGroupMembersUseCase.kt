package it.stamp.domain.usecase.membership

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
            val user = authService.requireUser()

            val membership = membershipRepository.getUserMembership(user.id)

            memberService.getGroupMembers(membership.groupId)
        }
}