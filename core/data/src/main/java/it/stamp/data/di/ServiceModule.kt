package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.service.FederatedAuthService
import it.stamp.data.service.FirestoreGroupTransferService
import it.stamp.data.service.FirestoreUserOnboardingService
import it.stamp.domain.service.AuthService
import it.stamp.domain.service.GroupTransferService
import it.stamp.domain.service.UserOnboardingService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    abstract fun bindSignInService(service: FederatedAuthService): AuthService

    @Binds
    @Singleton
    abstract fun bindGroupMigrationService(service: FirestoreGroupTransferService): GroupTransferService

    @Binds
    @Singleton
    abstract fun bindUserOnboardingService(service: FirestoreUserOnboardingService): UserOnboardingService
}