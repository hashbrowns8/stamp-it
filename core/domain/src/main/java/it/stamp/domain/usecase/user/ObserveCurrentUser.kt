package it.stamp.domain.usecase.user

import it.stamp.domain.service.AuthenticationService
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUser @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    operator fun invoke(): Flow<User?> = authenticationService.currentUser
}