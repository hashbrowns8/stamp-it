package it.stamp.domain.repository

import it.stamp.model.ids.UserId
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observe(id: UserId): Flow<User?>

    suspend fun findById(id: UserId): User?
}