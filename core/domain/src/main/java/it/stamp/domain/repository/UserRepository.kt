package it.stamp.domain.repository

import it.stamp.domain.exception.UserNotFoundException
import it.stamp.model.ids.UserId
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observe(id: UserId): Flow<User?>

    suspend fun findById(id: UserId): User?

    suspend fun getById(id: UserId): User =
        findById(id) ?: throw UserNotFoundException()

    suspend fun getByIds(ids: List<UserId>): List<User>
}