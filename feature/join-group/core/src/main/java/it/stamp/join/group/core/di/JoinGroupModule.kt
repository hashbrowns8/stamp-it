package it.stamp.join.group.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import it.stamp.join.group.api.JoinGroupNavKey
import it.stamp.join.group.core.JoinGroupScreen
import it.stamp.navigation.EntryProviderInstaller
import it.stamp.navigation.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
object JoinGroupModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<JoinGroupNavKey> {
            JoinGroupScreen(
                onBackClick = navigator::navigateBack,
                onJoinGroupSuccess = { group ->
                    navigator.navigateBack()
                },
            )
        }
    }
}