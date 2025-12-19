package it.stamp.data.firestore.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthenticationProvider @Inject constructor(
    private val auth: FirebaseAuth
) {
    val user: Flow<FirebaseUser?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener {
                trySend(auth.currentUser)
            }

            auth.addAuthStateListener(listener)

            trySend(auth.currentUser)

            awaitClose {
                auth.removeAuthStateListener(listener)
            }
        }

    suspend fun signInWithGoogle(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        val result = auth.signInWithCredential(credential).await()

        return result.user ?: throw IllegalStateException("Firebase User is Null :-/")
    }

    fun signOut() {
        auth.signOut()
    }
}