package it.stamp.data.repository

import it.stamp.data.firestore.mapper.GroupMapper
import it.stamp.data.firestore.source.GroupFirestoreDataSource
import it.stamp.domain.exception.GroupNotFoundException
import it.stamp.domain.repository.GroupRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import javax.inject.Inject

class GroupDataRepository @Inject constructor(
    private val dataSource: GroupFirestoreDataSource,
) : GroupRepository {

    override suspend fun getGroupById(groupId: GroupId): Result<Group> =
        runCatching {
            dataSource.read(groupId.value)
                ?.let(GroupMapper::toDomainModel)
                ?: throw GroupNotFoundException()
        }
}