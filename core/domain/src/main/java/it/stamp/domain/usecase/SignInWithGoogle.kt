package it.stamp.domain.usecase

import it.stamp.domain.repository.AuthenticationRepository
import it.stamp.model.user.User
import javax.inject.Inject

interface SignInWithGoogle {
    suspend operator fun invoke(): Result<User>
}

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthenticationRepository,
) : SignInWithGoogle {
    override suspend fun invoke(): Result<User> = repository.signInWithGoogle()
}