package it.stamp.domain.usecase

import it.stamp.domain.repository.GroupRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetGroupByIdUseCase @Inject constructor(
    private val groupRepository: GroupRepository,
) {
    suspend operator fun invoke(groupId: GroupId): Result<Group> =
        groupRepository.getGroupById(groupId)
}