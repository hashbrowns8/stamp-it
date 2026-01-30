package it.stamp.domain.service

import it.stamp.domain.exception.NotAuthenticatedException
import it.stamp.model.authentication.AuthenticationState
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.user.User
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationService {
    val state: StateFlow<AuthenticationState>

    val currentUser: StateFlow<User?>

    fun requireUser(): User = currentUser.value ?: throw NotAuthenticatedException()

    suspend fun signInWith(
        identityProvider: IdentityProvider,
        idToken: String,
    ): User

    suspend fun signOut()
}