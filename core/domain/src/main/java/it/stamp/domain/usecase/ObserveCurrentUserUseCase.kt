package it.stamp.domain.usecase

import it.stamp.domain.service.AuthenticationService
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveCurrentUserUseCase @Inject constructor(
    private val authenticationService: AuthenticationService,
) {
    operator fun invoke(): Flow<User?> = authenticationService.user
}