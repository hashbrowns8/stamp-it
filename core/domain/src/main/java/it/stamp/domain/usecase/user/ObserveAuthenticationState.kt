package it.stamp.domain.usecase.user

import it.stamp.domain.service.AuthenticationService
import it.stamp.model.authentication.AuthenticationState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveAuthenticationState @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    operator fun invoke(): Flow<AuthenticationState> = authenticationService.observeAuthenticationState()
}