package it.stamp.domain.usecase

import it.stamp.domain.service.AuthenticationService
import it.stamp.model.authentication.AuthenticationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthenticationService,
) {
    suspend operator fun invoke(idToken: String): AuthenticationResult =
        withContext(Dispatchers.IO) {
            repository.signInWithGoogle(idToken)
        }
}