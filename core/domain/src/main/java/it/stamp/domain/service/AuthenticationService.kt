package it.stamp.domain.service

import it.stamp.model.authentication.AuthenticationResult
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {
    val me: Flow<User?>

    suspend fun signInWithGoogle(idToken: String): AuthenticationResult

    fun signOut()
}