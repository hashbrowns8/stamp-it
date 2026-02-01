package it.stamp.domain.usecase.user

import it.stamp.domain.service.AuthenticationService
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInWithGoogle @Inject constructor(
    private val authenticationService: AuthenticationService,
) {
    suspend operator fun invoke(idToken: String): Result<User> =
        runCatching {
            authenticationService.signInWith(IdentityProvider.GOOGLE, idToken)
        }
}