package it.stamp.domain.repository

import it.stamp.model.ids.UserId
import it.stamp.model.user.User

interface UserRepository {
    suspend fun getUserById(id: UserId): Result<User?>
}