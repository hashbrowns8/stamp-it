package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.repository.FirebaseAuthenticationRepository
import it.stamp.domain.repository.AuthenticationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        repository: FirebaseAuthenticationRepository
    ): AuthenticationRepository
}