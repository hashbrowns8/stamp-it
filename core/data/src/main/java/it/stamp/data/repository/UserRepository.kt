package it.stamp.data.repository

import it.stamp.data.firestore.mapper.UserMapper
import it.stamp.data.firestore.source.UserFirestoreDataSource
import it.stamp.domain.exception.UserNotFoundException
import it.stamp.domain.repository.UserRepository
import it.stamp.model.ids.UserId
import it.stamp.model.user.User
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val dataSource: UserFirestoreDataSource,
) : UserRepository {

    override suspend fun getUserById(id: UserId): Result<User> =
        runCatching {
            dataSource
                .read(id.value)
                ?.let(UserMapper::toDomainModel)
                ?: throw UserNotFoundException()
        }
}