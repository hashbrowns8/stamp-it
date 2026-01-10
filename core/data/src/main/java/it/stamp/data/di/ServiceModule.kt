package it.stamp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.stamp.data.service.FederatedSignInService
import it.stamp.data.service.FirestoreLeaderboardService
import it.stamp.data.service.FirestoreMemberService
import it.stamp.data.service.FirestoreSignUpService
import it.stamp.domain.service.LeaderboardService
import it.stamp.domain.service.MemberService
import it.stamp.domain.service.SignInService
import it.stamp.domain.service.SignUpService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    abstract fun bindSignInService(service: FederatedSignInService): SignInService

    @Binds
    @Singleton
    abstract fun bindLeaderboardService(service: FirestoreLeaderboardService): LeaderboardService

    @Binds
    @Singleton
    abstract fun bindMemberService(service: FirestoreMemberService): MemberService

    @Binds
    @Singleton
    abstract fun bindSignUpService(service: FirestoreSignUpService): SignUpService
}