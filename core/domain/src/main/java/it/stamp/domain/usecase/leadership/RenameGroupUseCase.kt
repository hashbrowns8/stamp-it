package it.stamp.domain.usecase.leadership

import it.stamp.domain.exception.LeadershipException
import it.stamp.domain.repository.GroupRepository
import it.stamp.domain.usecase.membership.GetMyMembershipUseCase
import javax.inject.Inject

class RenameGroupUseCase @Inject constructor(
    private val getMyMembershipUseCase: GetMyMembershipUseCase,
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke(groupName: String): Result<Unit> = runCatching {
        val membership = getMyMembershipUseCase()
            .getOrThrow()

        require(membership.isLeader) {
            throw LeadershipException.Unauthorized()
        }

        require(groupName.isNotBlank()) // TODO

        groupRepository.rename(membership.groupId, groupName)
    }
}