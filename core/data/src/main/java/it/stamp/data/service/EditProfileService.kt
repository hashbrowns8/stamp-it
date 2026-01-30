package it.stamp.data.service

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.mapper.AvatarMapper
import it.stamp.data.firestore.model.FirestoreGroup
import it.stamp.data.firestore.model.FirestoreMembership
import it.stamp.data.firestore.model.FirestoreUser
import it.stamp.data.firestore.source.GroupFirestoreDataSource
import it.stamp.data.firestore.source.MembershipFirestoreDataSource
import it.stamp.data.firestore.source.UserFirestoreDataSource
import it.stamp.data.firestore.util.groupsCollection
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.usersCollection
import it.stamp.domain.exception.GroupNotFoundException
import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.domain.exception.UserNotFoundException
import it.stamp.domain.service.EditProfileService
import it.stamp.model.membership.Group
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreEditProfileService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userFirestoreDataSource: UserFirestoreDataSource,
    private val membershipFirestoreDataSource: MembershipFirestoreDataSource,
    private val groupFirestoreDataSource: GroupFirestoreDataSource,
) : EditProfileService {

    override suspend fun update(user: User?, group: Group?) {
        val (user, membership) = if (user == null) {
            null to null
        } else {
            applyUserProfileChanges(user)
        }

        val group = if (group == null) {
            null
        } else {
            applyGroupNameChanges(group)
        }

        firestore.run {
            runBatch { batch ->
                if (user != null && membership != null) {
                    batch.set(usersCollection.document(user.userId), user)
                    batch.set(membershipsCollection.document(membership.membershipId), membership)
                }

                if (group != null) {
                    batch.set(groupsCollection.document(group.groupId), group)
                }
            }
        }.await()
    }

    private suspend inline fun applyUserProfileChanges(
        user: User
    ): Pair<FirestoreUser, FirestoreMembership> = with(user) {
        val userFirestore = userFirestoreDataSource.find(id.value)
            ?: throw UserNotFoundException()

        val membership = membershipFirestoreDataSource.findUserMembership(id)
            ?: throw MembershipNotFoundException()

        userFirestore.run {
            val (nickname, nicknameChangedAt) = if (nickname == displayName.value) {
                nickname to nicknameChangedAt
            } else {
                displayName.value to Timestamp.now()
            }

            val profileImage = user.avatar.let(AvatarMapper::toFirestoreModel)

            Pair(
                userFirestore.copy(
                    nickname = nickname,
                    nicknameChangedAt = nicknameChangedAt,
                    profileImage = profileImage,
                ),
                membership.copy(
                    nickname = nickname,
                    profileImage = profileImage,
                ),
            )
        }
    }

    private suspend fun applyGroupNameChanges(group: Group): FirestoreGroup = with(group) {
        groupFirestoreDataSource.find(id.value)
            ?.copy(
                name = name,
                nameChangedAt = Timestamp.now(),
            )
            ?: throw GroupNotFoundException()
    }
}