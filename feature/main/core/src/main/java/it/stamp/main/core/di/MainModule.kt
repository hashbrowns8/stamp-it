package it.stamp.main.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import it.stamp.main.MainNavKey
import it.stamp.main.core.MainScreen
import it.stamp.navigation.EntryProviderInstaller
import it.stamp.navigation.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<MainNavKey> {
            MainScreen(navigator)
        }
    }
}