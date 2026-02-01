package it.stamp.data.service

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.firestore.mapper.toFirestoreModel
import it.stamp.data.firestore.source.MembershipFirestoreDataSource
import it.stamp.data.firestore.util.membershipsCollection
import it.stamp.data.firestore.util.usersCollection
import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.domain.service.UserProfileService
import it.stamp.model.ids.UserId
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserProfileService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val membershipFirestoreDataSource: MembershipFirestoreDataSource,
) : UserProfileService {

    override suspend fun updateProfile(
        id: UserId,
        avatar: Avatar?,
        displayName: DisplayName?
    ) {
        firestore.run {
            val membership = membershipFirestoreDataSource.findUserMembership(id)
                ?: throw MembershipNotFoundException()

            val avatar = avatar?.toFirestoreModel()

            runBatch { batch ->
                usersCollection.document(id.value)
                    .let { documentReference ->
                        val data = buildMap {
                            if (displayName != null) {
                                put(FIELD_NICKNAME, displayName.value)
                                put(FIELD_NICKNAME_CHANGED_AT, FieldValue.serverTimestamp())
                            }

                            if (avatar != null) {
                                put(FIELD_PROFILE_IMAGE, avatar)
                            }
                        }

                        batch.update(documentReference, data)
                    }

                membershipsCollection.document(membership.membershipId)
                    .let { documentReference ->
                        membership.copy()
                        batch.set(
                            documentReference,
                            membership.copy(
                                nickname = displayName?.value ?: membership.nickname,
                                profileImage = avatar ?: membership.profileImage,
                            )
                        )
                    }
            }
        }.await()
    }

    companion object {
        private const val FIELD_NICKNAME = "nickname"
        private const val FIELD_NICKNAME_CHANGED_AT = "nicknameChangedAt"
        private const val FIELD_PROFILE_IMAGE = "profileImage"
    }
}