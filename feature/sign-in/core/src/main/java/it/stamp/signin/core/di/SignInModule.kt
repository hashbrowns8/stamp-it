package it.stamp.signin.core.di

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay
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
        entry<SignInNavKey>(
            metadata = NavDisplay.transitionSpec {
                fadeIn(tween(1000)) togetherWith fadeOut(tween(300))
            } + NavDisplay.popTransitionSpec {
                EnterTransition.None togetherWith ExitTransition.None
            } + NavDisplay.predictivePopTransitionSpec {
                EnterTransition.None togetherWith ExitTransition.None
            },
        ) {
            SignInScreen(
                onSignInSuccess = {
                    // navigator.navigateBack()
                    navigator.navigate(MainNavKey)
                }
            )
        }
    }
}