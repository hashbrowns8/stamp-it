package it.stamp.domain.usecase.user

import it.stamp.domain.service.AuthService
import it.stamp.model.authentication.AuthState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveAuthenticationStateUseCase @Inject constructor(
    private val authService: AuthService
) {
    operator fun invoke(): Flow<AuthState> = authService.authState
}