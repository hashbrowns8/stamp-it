package it.stamp.data.model

import com.google.firebase.Timestamp
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId

data class FirestoreUser(
    val userId: String = String(),
    val groupId: String = String(),
    val nickname: String = String(),
    val profileImage: String = String(),
    val nicknameChangedAt: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
) {
    constructor(
        id: UserId,
        groupId: GroupId,
        displayName: String,
        avatar: String?,
    ) : this(
        userId = id.value,
        groupId = groupId.value,
        nickname = displayName,
        profileImage = avatar ?: DEFAULT_AVATAR,
    )

    companion object {
        const val DEFAULT_AVATAR = "profileImage1"
    }
}