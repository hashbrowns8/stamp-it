package it.stamp.domain.usecase.group

import it.stamp.domain.exception.GroupNotFoundException
import it.stamp.domain.repository.GroupRepository
import it.stamp.domain.usecase.membership.GetMyMembershipUseCase
import it.stamp.model.membership.Group
import javax.inject.Inject

class GetMyGroupUseCase @Inject constructor(
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke(): Result<Group> =
        runCatching {
            val membership = getMyMembershipUseCase().getOrThrow()

            groupRepository.findById(membership.groupId)
                ?: throw GroupNotFoundException()
        }
}