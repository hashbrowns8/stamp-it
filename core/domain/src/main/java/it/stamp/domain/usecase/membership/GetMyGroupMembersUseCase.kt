package it.stamp.domain.usecase.membership

import it.stamp.domain.service.MemberService
import it.stamp.domain.usecase.group.GetMyGroupUseCase
import it.stamp.model.membership.Member
import javax.inject.Inject

class GetMyGroupMembersUseCase @Inject constructor(
    private val getMyGroupUseCase: GetMyGroupUseCase,
    private val memberService: MemberService,
) {
    suspend operator fun invoke(): Result<List<Member>> =
        runCatching {
            getMyGroupUseCase()
                .getOrThrow()
                .let { group ->
                    memberService.getMembers(group.id)
                }
        }
}