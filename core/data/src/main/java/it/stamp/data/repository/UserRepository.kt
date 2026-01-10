package it.stamp.data.repository

import it.stamp.data.firestore.mapper.UserMapper
import it.stamp.data.firestore.source.FirestoreUserDataSource
import it.stamp.domain.repository.UserRepository
import it.stamp.model.ids.UserId
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val dataSource: FirestoreUserDataSource,
) : UserRepository {

    override fun observe(id: UserId): Flow<User?> =
        dataSource.observe(id.value)
            .map { user ->
                user?.let(UserMapper::toDomainModel)
            }

    override suspend fun findById(id: UserId): User? =
        dataSource.read(id.value)
            ?.let(UserMapper::toDomainModel)
}