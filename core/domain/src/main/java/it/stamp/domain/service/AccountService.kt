package it.stamp.domain.service

import it.stamp.model.ids.UserId
import it.stamp.model.user.User

interface AccountService {

    suspend fun registerAccount(userId: UserId): User

    suspend fun deleteAccount(userId: UserId)
}