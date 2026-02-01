package it.stamp.domain.usecase.membership

import it.stamp.domain.service.MembershipService
import it.stamp.model.ids.GroupId
import javax.inject.Inject

class JoinGroupUseCase @Inject constructor(
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val membershipService: MembershipService,
) {
    suspend operator fun invoke(targetGroupId: GroupId): Result<Unit> =
        runCatching {
            getMyMembershipUseCase()
                .getOrThrow()
                .let { currentMembership ->
                    membershipService.joinGroup(currentMembership, targetGroupId)
                }
        }
}