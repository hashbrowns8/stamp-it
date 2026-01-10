package it.stamp.data.authentication

import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.ids.UserId

interface IdentityVerifier {

    val type: IdentityProvider

    suspend fun signInWith(idToken: String): UserId
}