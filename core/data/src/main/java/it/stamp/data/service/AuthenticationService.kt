package it.stamp.data.service

import it.stamp.common.di.ApplicationScope
import it.stamp.common.di.IODispatcher
import it.stamp.data.authentication.IdentityVerifier
import it.stamp.data.authentication.UserSession
import it.stamp.data.authentication.UserSessionManager
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.service.UserProvisioningService
import it.stamp.model.authentication.AuthenticationState
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.user.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class FederatedAuthenticationService @Inject constructor(
    private val identityVerifiers: Map<IdentityProvider, @JvmSuppressWildcards IdentityVerifier>,
    private val userRepository: UserRepository,
    private val userProvisioningService: UserProvisioningService,
    private val sessionManager: UserSessionManager,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    @ApplicationScope coroutineScope: CoroutineScope,
) : AuthenticationService {
    override val state: StateFlow<AuthenticationState> = sessionManager.session
        .map { session ->
            if (session == null) {
                AuthenticationState.Unauthenticated
            } else {
                AuthenticationState.Authenticated(session.userId)
            }
        }
        .flowOn(coroutineDispatcher)
        .stateIn(coroutineScope, SharingStarted.Eagerly, AuthenticationState.Unknown)

    override val currentUser: StateFlow<User?> = state.flatMapLatest { state ->
        when (state) {
            is AuthenticationState.Authenticated -> userRepository.observe(state.userId)
            else -> flowOf(null)
        }
    }.flowOn(coroutineDispatcher)
        .stateIn(coroutineScope, SharingStarted.Eagerly, null)

    override suspend fun signInWith(identityProvider: IdentityProvider, idToken: String): User {
        val identityVerifier = identityVerifiers[identityProvider]
            ?: throw UnsupportedOperationException()

        val userId = identityVerifier.signInWith(idToken)

        val user = userRepository.findById(userId)
            ?: userProvisioningService.provision(userId)

        sessionManager.saveSession(UserSession(user.id, identityProvider))

        return user
    }

    override suspend fun signOut() {
        sessionManager.clearSession()
    }
}