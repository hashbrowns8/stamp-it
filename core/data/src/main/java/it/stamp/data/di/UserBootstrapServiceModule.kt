package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.bootstrap.FirestoreUserBootstrapService
import it.stamp.domain.service.UserBootstrapService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserBootstrapServiceModule {
    @Binds
    @Singleton
    abstract fun bindUserBootstrapService(
        transactionManager: FirestoreUserBootstrapService
    ): UserBootstrapService
}