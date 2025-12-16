package it.stamp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import it.stamp.data.model.FirestoreUser
import it.stamp.data.util.user
import it.stamp.data.util.userDocument
import it.stamp.domain.repository.UserRepository
import it.stamp.model.ids.UserId
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUser(id: UserId): User =
        firestore.userDocument(id.value)
            .get()
            .await()
}