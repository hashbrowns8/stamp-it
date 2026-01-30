package it.stamp.invite.member.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import it.stamp.invite.member.InviteMemberNavKey
import it.stamp.invite.member.core.InviteMemberScreen
import it.stamp.navigation.EntryProviderInstaller
import it.stamp.navigation.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
object InviteMemberModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<InviteMemberNavKey> {
            InviteMemberScreen(
                onBackClick = {
                    navigator.navigateBack()
                }
            )
        }
    }
}