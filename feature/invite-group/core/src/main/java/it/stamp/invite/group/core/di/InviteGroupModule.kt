package it.stamp.invite.group.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import it.stamp.invite.group.InviteGroupNavKey
import it.stamp.invite.group.core.InviteGroupScreen
import it.stamp.navigation.EntryProviderInstaller
import it.stamp.navigation.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
object InviteGroupModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<InviteGroupNavKey> {
            InviteGroupScreen(
                onBackClick = {
                    navigator.navigateBack()
                }
            )
        }
    }
}