package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.service.DefaultCurrentUserContextService
import it.stamp.data.service.DefaultMemberService
import it.stamp.data.service.FederatedAuthenticationService
import it.stamp.data.service.FirestoreAccountService
import it.stamp.data.service.FirestoreLeadershipService
import it.stamp.data.service.FirestoreMembershipService
import it.stamp.data.service.FirestoreUserProfileService
import it.stamp.domain.service.AccountService
import it.stamp.domain.service.AuthenticationService
import it.stamp.domain.service.CurrentUserContextService
import it.stamp.domain.service.LeadershipService
import it.stamp.domain.service.MemberService
import it.stamp.domain.service.MembershipService
import it.stamp.domain.service.UserProfileService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    abstract fun bindAccountService(service: FirestoreAccountService): AccountService

    @Binds
    @Singleton
    abstract fun bindAuthenticationService(service: FederatedAuthenticationService): AuthenticationService

    @Binds
    @Singleton
    abstract fun bindCurrentUserContextService(service: DefaultCurrentUserContextService): CurrentUserContextService

    @Binds
    @Singleton
    abstract fun bindGroupMemberService(service: DefaultMemberService): MemberService

    @Binds
    @Singleton
    abstract fun bindLeadershipService(service: FirestoreLeadershipService): LeadershipService

    @Binds
    @Singleton
    abstract fun bindMembershipService(service: FirestoreMembershipService): MembershipService

    @Binds
    @Singleton
    abstract fun bindUserProfileService(service: FirestoreUserProfileService): UserProfileService
}