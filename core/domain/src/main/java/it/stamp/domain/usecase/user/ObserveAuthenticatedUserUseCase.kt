package it.stamp.domain.usecase.user

import it.stamp.model.authentication.currentUser
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAuthenticatedUserUseCase @Inject constructor(
    private val observeAuthenticationStateUseCase: ObserveAuthenticationStateUseCase,
) {
    operator fun invoke(): Flow<User?> = observeAuthenticationStateUseCase()
        .map { authenticationState ->
            authenticationState.currentUser
        }
}