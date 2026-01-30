package it.stamp.membership.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import it.stamp.membership.MembershipNavKey
import it.stamp.membership.core.MembershipScreen
import it.stamp.navigation.EntryProviderInstaller
import it.stamp.navigation.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
object MembershipModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<MembershipNavKey> {
            MembershipScreen(
                onBack = {
                    navigator.navigateBack()
                },
            )
        }
    }
}