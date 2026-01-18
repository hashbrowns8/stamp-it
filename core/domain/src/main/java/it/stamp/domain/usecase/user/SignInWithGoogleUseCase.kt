package it.stamp.domain.usecase.user

import it.stamp.domain.service.AuthService
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInWithGoogleUseCase @Inject constructor(
    private val authService: AuthService,
) {
    suspend operator fun invoke(idToken: String): Result<User> =
        runCatching {
            authService.signInWith(IdentityProvider.GOOGLE, idToken)
        }
}