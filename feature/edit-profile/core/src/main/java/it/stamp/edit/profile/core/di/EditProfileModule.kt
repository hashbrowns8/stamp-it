package it.stamp.edit.profile.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import it.stamp.edit.profile.EditProfileNavKey
import it.stamp.edit.profile.core.EditProfileScreen
import it.stamp.navigation.EntryProviderInstaller
import it.stamp.navigation.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
object EditProfileModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<EditProfileNavKey> {
            EditProfileScreen(
                onBack = {
                    navigator.navigateBack()
                },
            )
        }
    }
}