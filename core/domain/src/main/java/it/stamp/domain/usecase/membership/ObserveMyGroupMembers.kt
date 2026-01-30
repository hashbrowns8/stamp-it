package it.stamp.domain.usecase.membership

import it.stamp.domain.service.GroupMemberService
import it.stamp.model.membership.Member
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMyGroupMembers @Inject constructor(
    private val memberService: GroupMemberService,
) {
    operator fun invoke(): Flow<List<Member>> = memberService.observeMyGroupMembers()
}