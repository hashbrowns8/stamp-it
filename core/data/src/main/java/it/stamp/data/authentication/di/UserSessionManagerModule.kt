package it.stamp.data.authentication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.authentication.DataStoreUserSessionManager
import it.stamp.data.authentication.UserSessionManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserSessionManagerModule {
    @Binds
    @Singleton
    abstract fun bindUserSessionManager(sessionManager: DataStoreUserSessionManager): UserSessionManager
}