package it.stamp.data.repository

import it.stamp.data.firestore.mapper.toDomainModel
import it.stamp.data.firestore.source.MembershipFirestoreDataSource
import it.stamp.domain.repository.MembershipRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MembershipDataRepository @Inject constructor(
    private val dataSource: MembershipFirestoreDataSource,
) : MembershipRepository {

    override suspend fun findUserMembership(userId: UserId): Membership? =
        dataSource.findUserMembership(userId)?.toDomainModel()

    override fun observeUserMembership(userId: UserId): Flow<Membership?> =
        dataSource.observeUserMembership(userId)
            .map { membership ->
                membership?.toDomainModel()
            }

    override suspend fun getGroupMemberships(groupId: GroupId): List<Membership> =
        dataSource.getGroupMemberships(groupId)
            .map { membership ->
                membership.toDomainModel()
            }

    override fun observeGroupMemberships(groupId: GroupId): Flow<List<Membership>> =
        dataSource.observeGroupMemberships(groupId)
            .map { memberships ->
                memberships.map { membership ->
                    membership.toDomainModel()
                }
            }

    override suspend fun getGroupMemberCount(groupId: GroupId): Int =
        dataSource.getGroupMemberCount(groupId)
}