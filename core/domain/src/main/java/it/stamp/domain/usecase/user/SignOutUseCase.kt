package it.stamp.domain.usecase.user

import it.stamp.domain.service.AuthenticationService
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
) {
    suspend operator fun invoke() = runCatching {
        authenticationService.signOut()
    }
}