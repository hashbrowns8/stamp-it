package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.repository.FirebaseAuthenticationRepository
import it.stamp.data.repository.FirebaseMembershipRepository
import it.stamp.domain.repository.AuthenticationRepository
import it.stamp.domain.repository.MembershipRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        repository: FirebaseAuthenticationRepository
    ): AuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindMembershipRepository(
        repository: FirebaseMembershipRepository
    ): MembershipRepository
}