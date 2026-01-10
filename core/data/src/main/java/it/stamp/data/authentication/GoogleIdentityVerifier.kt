package it.stamp.data.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import it.stamp.domain.exception.GoogleSignInException
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.ids.UserId
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleIdentityVerifier @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : IdentityVerifier {

    override val type: IdentityProvider = IdentityProvider.GOOGLE

    override suspend fun signInWith(idToken: String): UserId {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        val result = firebaseAuth
            .signInWithCredential(credential)
            .await()

        return result.user
            ?.uid
            ?.let(::UserId)
            ?: throw GoogleSignInException()
    }
}