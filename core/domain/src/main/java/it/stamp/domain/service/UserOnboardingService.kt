package it.stamp.domain.service

import it.stamp.model.ids.UserId
import it.stamp.model.user.User

interface UserOnboardingService {
    suspend fun onboard(id: UserId): User
}