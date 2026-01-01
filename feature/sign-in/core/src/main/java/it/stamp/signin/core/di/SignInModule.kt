package it.stamp.signin.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import it.stamp.main.MainNavKey
import it.stamp.navigation.EntryProviderInstaller
import it.stamp.navigation.Navigator
import it.stamp.signin.SignInNavKey
import it.stamp.signin.core.SignInScreen

@Module
@InstallIn(ActivityRetainedComponent::class)
object SignInModule {
    @Provides
    @IntoSet
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<SignInNavKey> {
            SignInScreen(
                onSignInSuccess = {
                    navigator.navigateBack()
                    navigator.navigate(MainNavKey)
                }
            )
        }
    }
}