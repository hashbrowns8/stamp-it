package it.stamp.domain.usecase

import it.stamp.domain.service.SignInService
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveCurrentUserUseCase @Inject constructor(
    private val signInService: SignInService,
) {
    operator fun invoke(): Flow<User?> = signInService.currentUser
}