package it.stamp.domain.usecase.membership

import it.stamp.domain.service.GroupMemberService
import it.stamp.model.membership.Member
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMyGroupMembersUseCase @Inject constructor(
    private val memberService: GroupMemberService,
) {
    suspend operator fun invoke(): Result<List<Member>> =
        runCatching {
            memberService.getMyGroupMembers()
        }
}