package it.stamp.domain.service

import it.stamp.model.authentication.AuthenticationState
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {

    fun observeAuthenticationState(): Flow<AuthenticationState>

    suspend fun requireAuthenticated(): AuthenticationState.Authenticated

    suspend fun signInWith(
        identityProvider: IdentityProvider,
        idToken: String,
    ): User

    suspend fun signOut()
}