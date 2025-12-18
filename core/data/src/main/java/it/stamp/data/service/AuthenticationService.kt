package it.stamp.data.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import it.stamp.data.model.FirestoreUser
import it.stamp.data.model.toDomainUser
import it.stamp.data.util.user
import it.stamp.data.util.userDocument
import it.stamp.domain.service.AuthenticationService
import it.stamp.model.authentication.AuthenticationResult
import it.stamp.model.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthenticationService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthenticationService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val user: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            trySend(auth.currentUser)
        }

        auth.addAuthStateListener(listener)

        trySend(auth.currentUser)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }.flatMapLatest { user ->
        if (user == null) {
            flowOf(null)
        } else {
            firestore.userDocument(user.uid)
                .snapshots()
                .map {
                    it.toObject<FirestoreUser>()
                        ?.toDomainUser()
                        ?: user.toDomainUser()
                }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun signInWithGoogle(idToken: String): AuthenticationResult = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        val result = auth.signInWithCredential(credential).await()

        val userFirebase = result.user ?: throw IllegalStateException("Firebase User is Null :-(")

        val userFirestore = firestore.user(userFirebase.uid)

        val user = userFirestore ?: userFirebase.toDomainUser()

        val isNewUser = userFirestore == null // needs Bootstrap

        AuthenticationResult.Authenticated(user, isNewUser)
    }.getOrElse { throwable ->
        AuthenticationResult.Failure(throwable)
    }

    override fun signOut(): Result<Unit> = runCatching {
        auth.signOut()
    }
}