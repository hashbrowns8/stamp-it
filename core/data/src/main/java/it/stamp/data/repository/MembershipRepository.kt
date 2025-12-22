package it.stamp.data.repository

import it.stamp.data.firestore.mapper.MembershipMapper
import it.stamp.data.firestore.source.MembershipFirestoreDataSource
import it.stamp.domain.repository.MembershipRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MembershipDataRepository @Inject constructor(
    private val dataSource: MembershipFirestoreDataSource,
) : MembershipRepository {

    override fun observeMembershipsByGroup(groupId: GroupId): Flow<List<Membership>> =
        dataSource
            .observeGroupMemberships(groupId)
            .map { memberships ->
                memberships.map(MembershipMapper::toDomainModel)
            }
            .flowOn(Dispatchers.IO)

    override suspend fun getMembershipsByGroup(groupId: GroupId): Result<List<Membership>> =
        runCatching {
            dataSource
                .getGroupMemberships(groupId)
                .map(MembershipMapper::toDomainModel)
        }

    override fun observeMembershipByUser(userId: UserId): Flow<Membership> =
        dataSource
            .observeUserMembership(userId)
            .map(MembershipMapper::toDomainModel)
            .flowOn(Dispatchers.IO)

    override suspend fun getMembershipByUser(userId: UserId): Result<Membership> =
        runCatching {
            dataSource
                .getUserMembership(userId)
                .let(MembershipMapper::toDomainModel)
        }
}