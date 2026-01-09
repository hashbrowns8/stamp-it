package it.stamp.domain.usecase

import it.stamp.model.ids.GroupId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JoinGroupUseCase @Inject constructor(
) {
    suspend operator fun invoke(groupId: GroupId) {
    }
}