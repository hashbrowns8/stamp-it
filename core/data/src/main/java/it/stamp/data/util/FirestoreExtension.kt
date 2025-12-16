package it.stamp.data.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await

val FirebaseFirestore.usersCollection: CollectionReference
    get() = collection("users")

fun FirebaseFirestore.userDocument(id: String): DocumentReference = usersCollection.document(id)

suspend fun FirebaseFirestore.user(id: String): User? = runCatching {
    userDocument(id)
        .get()
        .await()
        .user
}.getOrNull()

private val DocumentSnapshot.user: User
    get() = User(UserId(id), displayName, avatar)

private val DocumentSnapshot.displayName: String
    get() = getString("nickname") ?: ":-/" // TODO

private val DocumentSnapshot.avatar: String?
    get() = getString("profileImage")

val FirebaseFirestore.groupsCollection: CollectionReference
    get() = collection("groups")

fun FirebaseFirestore.groupDocument(id: String): DocumentReference = groupsCollection.document(id)

val FirebaseFirestore.membershipsCollection: CollectionReference
    get() = collection("memberships")

fun FirebaseFirestore.membershipDocument(id: String): DocumentReference = membershipsCollection.document(id)

suspend fun FirebaseFirestore.groupMembershipDocuments(groupId: GroupId): QuerySnapshot =
    membershipsCollection.whereEqualTo("groupId", groupId.value)
        .get()
        .await()

suspend fun FirebaseFirestore.userMembershipDocument(userId: UserId): QueryDocumentSnapshot =
    membershipsCollection.whereEqualTo("userId", userId.value)
        .get()
        .await()
        .single()