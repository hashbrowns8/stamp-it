package it.stamp.domain.service

import it.stamp.domain.exception.NotAuthenticatedException
import it.stamp.model.authentication.AuthState
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.user.User
import kotlinx.coroutines.flow.StateFlow

interface AuthService {
    val authState: StateFlow<AuthState>

    val currentUser: User?

    fun requireUser(): User = currentUser ?: throw NotAuthenticatedException()

    suspend fun signInWith(identityProvider: IdentityProvider, idToken: String): User

    suspend fun signOut()
}