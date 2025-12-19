package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.service.FirebaseAuthenticationService
import it.stamp.data.service.FirestoreUserBootstrapService
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.service.UserBootstrapService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    abstract fun bindAuthenticationService(
        service: FirebaseAuthenticationService
    ): AuthenticationService

    @Binds
    @Singleton
    abstract fun bindUserBootstrapService(
        service: FirestoreUserBootstrapService
    ): UserBootstrapService
}