package it.stamp.data.repository

import com.google.firebase.Timestamp
import it.stamp.data.firestore.mapper.GroupMapper
import it.stamp.data.firestore.source.GroupFirestoreDataSource
import it.stamp.domain.repository.GroupRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GroupDataRepository @Inject constructor(
    private val firestoreDataSource: GroupFirestoreDataSource,
) : GroupRepository {

    override suspend fun findById(id: GroupId): Group? =
        firestoreDataSource.find(id.value)
            ?.let(GroupMapper::toDomainModel)

    override suspend fun findByInviteCode(inviteCode: InviteCode): Group? =
        firestoreDataSource.findGroupByInviteCode(inviteCode)
            ?.let(GroupMapper::toDomainModel)

    override fun observe(id: GroupId): Flow<Group> =
        firestoreDataSource.observe(id.value)
            .filterNotNull()
            .map(GroupMapper::toDomainModel)

    override suspend fun update(group: Group) {
        firestoreDataSource.find(group.id.value)
            ?.copy(
                name = group.name,
                nameChangedAt = Timestamp.now(),
            )
            ?.let { data ->
                firestoreDataSource.update(data.groupId, data)
            }
    }
}