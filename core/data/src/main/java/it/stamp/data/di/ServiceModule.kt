package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.service.DefaultGroupMemberService
import it.stamp.data.service.FederatedAuthenticationService
import it.stamp.data.service.FirestoreEditProfileService
import it.stamp.data.service.FirestoreGroupTransferService
import it.stamp.data.service.FirestoreMembershipService
import it.stamp.data.service.FirestoreUserProvisioningService
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.service.EditProfileService
import it.stamp.domain.service.GroupMemberService
import it.stamp.domain.service.GroupTransferService
import it.stamp.domain.service.MembershipService
import it.stamp.domain.service.UserProvisioningService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    abstract fun bindAuthenticationService(service: FederatedAuthenticationService): AuthenticationService

    @Binds
    @Singleton
    abstract fun bindEditProfileService(service: FirestoreEditProfileService): EditProfileService

    @Binds
    @Singleton
    abstract fun bindGroupMemberService(service: DefaultGroupMemberService): GroupMemberService

    @Binds
    @Singleton
    abstract fun bindGroupTransferService(service: FirestoreGroupTransferService): GroupTransferService

    @Binds
    @Singleton
    abstract fun bindMembershipService(service: FirestoreMembershipService): MembershipService

    @Binds
    @Singleton
    abstract fun bindUserProvisioningService(service: FirestoreUserProvisioningService): UserProvisioningService
}