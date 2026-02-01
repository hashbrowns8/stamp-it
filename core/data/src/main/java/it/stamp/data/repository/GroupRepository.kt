package it.stamp.data.repository

import com.google.firebase.firestore.FieldValue
import it.stamp.data.firestore.mapper.toDomainModel
import it.stamp.data.firestore.source.GroupFirestoreDataSource
import it.stamp.domain.repository.GroupRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GroupDataRepository @Inject constructor(
    private val dataSource: GroupFirestoreDataSource,
) : GroupRepository {

    override suspend fun findById(id: GroupId): Group? =
        dataSource.find(id.value)?.toDomainModel()

    override suspend fun findByInviteCode(inviteCode: InviteCode): Group? =
        dataSource.findGroupByInviteCode(inviteCode)
            ?.toDomainModel()

    override fun observe(id: GroupId): Flow<Group?> =
        dataSource.observe(id.value)
            .map { group ->
                group?.toDomainModel()
            }

    override suspend fun rename(id: GroupId, name: String) {
        dataSource.update(
            id = id.value,
            data = buildMap {
                put(FIELD_NAME, name)
                put(FIELD_NAME_CHANGED_AT, FieldValue.serverTimestamp())
            },
        )
    }

    companion object {
        private const val FIELD_NAME = "name"
        private const val FIELD_NAME_CHANGED_AT = "nameChangedAt"
    }
}