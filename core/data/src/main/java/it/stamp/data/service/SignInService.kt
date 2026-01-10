package it.stamp.data.service

import it.stamp.data.authentication.IdentityVerifier
import it.stamp.data.authentication.UserSessionManager
import it.stamp.domain.repository.UserRepository
import it.stamp.domain.service.SignInService
import it.stamp.domain.service.SignUpService
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import javax.inject.Inject

class FederatedSignInService @Inject constructor(
    private val identityVerifiers: Map<IdentityProvider, @JvmSuppressWildcards IdentityVerifier>,
    private val userRepository: UserRepository,
    private val signUpService: SignUpService,
    private val sessionManager: UserSessionManager,
) : SignInService {

    override val currentUser: Flow<User?> = sessionManager.currentUserId
        .flatMapLatest { id ->
            if (id == null) {
                flowOf(null)
            } else {
                userRepository.observe(id)
            }
        }

    override suspend fun signInWith(
        identityProvider: IdentityProvider,
        idToken: String,
    ): User {
        val identityVerifier = identityVerifiers[identityProvider]
            ?: throw UnsupportedOperationException()

        val userId = identityVerifier.signInWith(idToken)

        Timber.d("user id = $userId")
        val user = userRepository.findById(userId)
            ?: signUpService.signUp(userId)

        Timber.d("user = $user")

        sessionManager.setUserId(user.id)

        Timber.d("session done")

        return user
    }

    override suspend fun signOut() {
        sessionManager.clearSession()
    }
}