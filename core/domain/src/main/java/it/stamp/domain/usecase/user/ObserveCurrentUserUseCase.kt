package it.stamp.domain.usecase.user

import it.stamp.model.authentication.AuthState
import it.stamp.model.authentication.currentUser
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
) {
    operator fun invoke(): Flow<User?> = observeAuthStateUseCase()
        .filterNot { authState ->
            authState == AuthState.Unknown
        }
        .map { authState ->
            authState.currentUser
        }
}