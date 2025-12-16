package it.stamp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.model.FirestoreMembership
import it.stamp.data.model.toDomainMembership
import it.stamp.data.util.groupMembershipDocuments
import it.stamp.data.util.userMembershipDocument
import it.stamp.domain.repository.MembershipRepository
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Membership
import timber.log.Timber
import javax.inject.Inject

class FirebaseMembershipRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : MembershipRepository {

    override suspend fun getGroupMemberships(groupId: GroupId): List<Membership> =
        firestore.groupMembershipDocuments(groupId)
            .mapNotNull { snapshot ->
                runCatching {
                    snapshot.toObject(FirestoreMembership::class.java)
                }.onFailure {
                    Timber.d(it) // TODO : Crashlytics
                }.getOrNull()
                    ?.toDomainMembership()
            }

    override suspend fun getUserMembership(userId: UserId): Membership =
        firestore.userMembershipDocument(userId)
            .toObject(FirestoreMembership::class.java)
            .toDomainMembership()
}