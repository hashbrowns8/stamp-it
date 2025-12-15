package it.stamp.domain.repository

import it.stamp.model.authentication.AuthenticationResult
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val user: Flow<User?>

    suspend fun signInWithGoogle(idToken: String): AuthenticationResult

    fun signOut(): Result<Unit>
}