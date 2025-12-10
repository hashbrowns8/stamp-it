package it.stamp.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import it.stamp.data.source.GoogleCredentialDataSource
import it.stamp.domain.repository.AuthenticationRepository
import it.stamp.model.user.User
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticationRepository(
    private val googleCredentialDataSource: GoogleCredentialDataSource,
) : AuthenticationRepository {

    override suspend fun signInWithGoogle(): Result<User> {
        val idToken = googleCredentialDataSource.getGoogleIdToken()

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        val result = Firebase.auth.signInWithCredential(credential).await()

        result.user
        TODO("Not yet implemented")
    }
}