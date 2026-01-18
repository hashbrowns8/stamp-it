package it.stamp.data.service

import it.stamp.common.di.ApplicationScope
import it.stamp.data.authentication.IdentityVerifier
import it.stamp.data.authentication.UserSessionManager
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.AuthService
import it.stamp.domain.service.UserOnboardingService
import it.stamp.model.authentication.AuthState
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.authentication.currentUser
import it.stamp.model.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class FederatedAuthService @Inject constructor(
    private val identityVerifiers: Map<IdentityProvider, @JvmSuppressWildcards IdentityVerifier>,
    private val userRepository: UserRepository,
    private val userOnboardingService: UserOnboardingService,
    private val sessionManager: UserSessionManager,
    @ApplicationScope coroutineScope: CoroutineScope,
) : AuthService {
    override val authState: StateFlow<AuthState> = sessionManager.userId
        .flatMapLatest { userId ->
            if (userId == null) {
                flowOf(AuthState.Unauthenticated)
            } else {
                userRepository.observe(userId)
                    .map { user ->
                        if (user == null) {
                            AuthState.Unauthenticated
                        } else {
                            AuthState.Authenticated(user)
                        }
                    }
            }
        }
        .stateIn(coroutineScope, SharingStarted.Eagerly, AuthState.Unknown)

    override val currentUser: User?
        get() = authState.value.currentUser

    override suspend fun signInWith(
        identityProvider: IdentityProvider,
        idToken: String,
    ): User {
        val identityVerifier = identityVerifiers[identityProvider]
            ?: throw UnsupportedOperationException()

        val userId = identityVerifier.signInWith(idToken)

        val user = userRepository.findById(userId)
            ?: userOnboardingService.onboard(userId)

        sessionManager.setUserId(user.id)

        return user
    }

    override suspend fun signOut() {
        sessionManager.clearSession()
    }
}