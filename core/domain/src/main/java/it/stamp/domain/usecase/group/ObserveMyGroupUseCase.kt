package it.stamp.domain.usecase.group

import it.stamp.domain.repository.GroupRepository
import it.stamp.domain.usecase.membership.ObserveMyMembershipUseCase
import it.stamp.model.membership.Group
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveMyGroupUseCase @Inject constructor(
    private val observeMyMembershipUseCase: ObserveMyMembershipUseCase,
    private val groupRepository: GroupRepository,
) {
    operator fun invoke(): Flow<Group?> = observeMyMembershipUseCase()
        .distinctUntilChangedBy { it?.groupId }
        .flatMapLatest { membership ->
            membership
                ?.groupId
                ?.let(groupRepository::observe)
                ?: flowOf(null)
        }
}