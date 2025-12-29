package it.stamp.domain.usecase

import it.stamp.domain.service.AuthenticationService
import it.stamp.model.authentication.AuthenticationResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthenticationService,
) {
    suspend operator fun invoke(idToken: String): AuthenticationResult =
        repository.signInWithGoogle(idToken)
}