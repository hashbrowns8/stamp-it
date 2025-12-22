package it.stamp.domain.usecase

import it.stamp.domain.service.AuthenticationService
import it.stamp.model.authentication.AuthenticationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveAuthenticationStateUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
) {
    operator fun invoke(): Flow<AuthenticationState> = authenticationService.me
        .map { user ->
            if (user == null) {
                AuthenticationState.NotAuthenticated
            } else {
                AuthenticationState.Authenticated(user)
            }
        }
}