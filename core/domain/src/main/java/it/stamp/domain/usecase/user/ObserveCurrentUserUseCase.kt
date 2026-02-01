package it.stamp.domain.usecase.user

import it.stamp.domain.service.CurrentUserContextService
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
    private val currentUserContextService: CurrentUserContextService
) {
    operator fun invoke(): Flow<User?> = currentUserContextService.observeUser()
}