package it.stamp.data.repository

import it.stamp.data.firestore.mapper.MembershipMapper
import it.stamp.data.firestore.source.MembershipFirestoreDataSource
import it.stamp.domain.repository.MembershipRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MembershipDataRepository @Inject constructor(
    private val firestoreDataSource: MembershipFirestoreDataSource,
) : MembershipRepository {

    override suspend fun findUserMembership(userId: UserId): Membership? =
        firestoreDataSource.findUserMembership(userId)
            ?.let(MembershipMapper::toDomainModel)

    override fun observeUserMembership(userId: UserId): Flow<Membership?> =
        firestoreDataSource.observeUserMembership(userId)
            .map { membership ->
                membership?.let(MembershipMapper::toDomainModel)
            }

    override suspend fun getGroupMemberships(groupId: GroupId): List<Membership> =
        firestoreDataSource.getGroupMemberships(groupId)
            .map(MembershipMapper::toDomainModel)

    override fun observeGroupMemberships(groupId: GroupId): Flow<List<Membership>> =
        firestoreDataSource.observeGroupMemberships(groupId)
            .map { memberships ->
                memberships.map(MembershipMapper::toDomainModel)
            }

    override suspend fun getGroupMemberCount(groupId: GroupId): Int =
        firestoreDataSource.getGroupMemberCount(groupId)

    override suspend fun updateMembership(membership: Membership) {
        TODO("Not yet implemented")
    }
}