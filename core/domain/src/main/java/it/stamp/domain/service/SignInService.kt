package it.stamp.domain.service

import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow

interface SignInService {
    val currentUser: Flow<User?>

    suspend fun signInWith(identityProvider: IdentityProvider, idToken: String): User

    suspend fun signOut()
}