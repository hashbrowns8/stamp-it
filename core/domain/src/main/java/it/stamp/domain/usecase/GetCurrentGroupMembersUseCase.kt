package it.stamp.domain.usecase

import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.model.membership.Member
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentGroupMembersUseCase @Inject constructor(
    private val observeCurrentMembershipUseCase: ObserveCurrentMembershipUseCase,
    private val getGroupMembersUseCase: GetGroupMembersUseCase,
) {
    suspend operator fun invoke(): Result<List<Member>> =
        runCatching {
            val membership = observeCurrentMembershipUseCase()
                .firstOrNull()
                ?: throw MembershipNotFoundException()

            getGroupMembersUseCase(membership.groupId)
                .getOrThrow()
        }
}