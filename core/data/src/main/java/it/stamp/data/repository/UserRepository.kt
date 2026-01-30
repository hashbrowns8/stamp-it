package it.stamp.data.repository

import it.stamp.data.firestore.mapper.UserMapper
import it.stamp.data.firestore.source.UserFirestoreDataSource
import it.stamp.domain.repository.UserRepository
import it.stamp.model.ids.UserId
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val firestoreDataSource: UserFirestoreDataSource,
) : UserRepository {

    override fun observe(id: UserId): Flow<User?> =
        firestoreDataSource.observe(id.value)
            .map { user ->
                user?.let(UserMapper::toDomainModel)
            }

    override suspend fun findById(id: UserId): User? =
        firestoreDataSource.find(id.value)
            ?.let(UserMapper::toDomainModel)

    override suspend fun getByIds(ids: List<UserId>): List<User> =
        firestoreDataSource.getByIds(ids)
            .map(UserMapper::toDomainModel)
}