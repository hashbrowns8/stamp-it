package it.stamp.domain.service

import it.stamp.model.ids.UserId
import it.stamp.model.user.User

interface UserProvisioningService {
    suspend fun provision(userId: UserId): User
}