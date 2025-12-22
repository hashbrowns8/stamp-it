package it.stamp.data.service

import it.stamp.data.firestore.mapper.UserMapper
import it.stamp.data.firestore.source.FirebaseAuthenticationProvider
import it.stamp.data.firestore.source.UserFirestoreDataSource
import it.stamp.domain.service.AuthenticationService
import it.stamp.model.authentication.AuthenticationResult
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirebaseAuthenticationService @Inject constructor(
    private val authenticationProvider: FirebaseAuthenticationProvider,
    private val userDataSource: UserFirestoreDataSource,
) : AuthenticationService {

    override val me: Flow<User?> = authenticationProvider.user
        .flatMapLatest { userFirebase ->
            if (userFirebase == null) return@flatMapLatest flowOf(null)

            userDataSource.observe(userFirebase.uid)
                .map { userFirestore ->
                    userFirestore?.let(UserMapper::toDomainModel)
                }
        }

    override suspend fun signInWithGoogle(idToken: String): AuthenticationResult = runCatching {
        val userFirebase = authenticationProvider.signInWithGoogle(idToken)

        val userFirestore = userDataSource.read(userFirebase.uid)

        val user = userFirestore?.let(UserMapper::toDomainModel)
            ?: UserMapper.toDomainModel(userFirebase)

        val isNewUser = userFirestore == null // needs Bootstrap

        AuthenticationResult.Authenticated(user, isNewUser)
    }.getOrElse { throwable ->
        AuthenticationResult.Failure(throwable)
    }

    override fun signOut() {
        authenticationProvider.signOut()
    }
}