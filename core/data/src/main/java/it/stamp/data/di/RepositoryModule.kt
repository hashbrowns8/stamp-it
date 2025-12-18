package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.service.FirebaseAuthenticationService
import it.stamp.data.repository.FirebaseMembershipRepository
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.repository.MembershipRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        repository: FirebaseAuthenticationService
    ): AuthenticationService

    @Binds
    @Singleton
    abstract fun bindMembershipRepository(
        repository: FirebaseMembershipRepository
    ): MembershipRepository
}