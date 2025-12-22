package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.repository.GroupDataRepository
import it.stamp.data.repository.MembershipDataRepository
import it.stamp.data.repository.MissionDataRepository
import it.stamp.data.repository.StampDataRepository
import it.stamp.data.repository.UserDataRepository
import it.stamp.domain.repository.GroupRepository
import it.stamp.domain.repository.MembershipRepository
import it.stamp.domain.repository.MissionRepository
import it.stamp.domain.repository.StampRepository
import it.stamp.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGroupRepository(repository: GroupDataRepository): GroupRepository

    @Binds
    @Singleton
    abstract fun bindMembershipRepository(repository: MembershipDataRepository): MembershipRepository

    @Binds
    @Singleton
    abstract fun bindMissionRepository(repository: MissionDataRepository): MissionRepository

    @Binds
    @Singleton
    abstract fun bindStampRepository(repository: StampDataRepository): StampRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(repository: UserDataRepository): UserRepository
}