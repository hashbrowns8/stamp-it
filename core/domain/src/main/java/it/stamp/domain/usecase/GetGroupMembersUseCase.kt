package it.stamp.domain.usecase

import it.stamp.domain.service.MemberService
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Member
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetGroupMembersUseCase @Inject constructor(
    private val memberService: MemberService,
) {
    suspend operator fun invoke(groupId: GroupId): Result<List<Member>> =
        memberService.getMembersByGroup(groupId)
}