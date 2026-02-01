package it.stamp.data.repository

import it.stamp.data.firestore.mapper.toDomainModel
import it.stamp.data.firestore.source.UserFirestoreDataSource
import it.stamp.domain.repository.UserRepository
import it.stamp.model.ids.UserId
import it.stamp.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val dataSource: UserFirestoreDataSource,
) : UserRepository {

    override fun observe(id: UserId): Flow<User?> =
        dataSource.observe(id.value)
            .map { user ->
                user?.toDomainModel()
            }

    override suspend fun findById(id: UserId): User? = dataSource.find(id.value)?.toDomainModel()

    override suspend fun getByIds(ids: List<UserId>): List<User> =
        dataSource.getByIds(ids)
            .map { user ->
                user.toDomainModel()
            }
}