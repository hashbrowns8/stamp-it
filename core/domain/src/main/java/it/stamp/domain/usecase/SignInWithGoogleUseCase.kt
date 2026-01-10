package it.stamp.domain.usecase

import it.stamp.domain.service.SignInService
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInWithGoogleUseCase @Inject constructor(
    private val signInService: SignInService,
) {
    suspend operator fun invoke(idToken: String): Result<User> =
        runCatching {
            signInService.signInWith(IdentityProvider.GOOGLE, idToken)
        }
}