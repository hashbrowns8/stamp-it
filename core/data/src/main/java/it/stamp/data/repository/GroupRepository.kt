package it.stamp.data.repository

import it.stamp.data.firestore.mapper.GroupMapper
import it.stamp.data.firestore.source.GroupFirestoreDataSource
import it.stamp.domain.repository.GroupRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode
import javax.inject.Inject

class GroupDataRepository @Inject constructor(
    private val firestoreDataSource: GroupFirestoreDataSource,
) : GroupRepository {

    override suspend fun findGroupByInviteCode(inviteCode: InviteCode): Group? =
        firestoreDataSource.getGroupByInviteCode(inviteCode)
            ?.let(GroupMapper::toDomainModel)

    override suspend fun findById(id: GroupId): Group? =
        firestoreDataSource.get(id.value)
            ?.let(GroupMapper::toDomainModel)
}