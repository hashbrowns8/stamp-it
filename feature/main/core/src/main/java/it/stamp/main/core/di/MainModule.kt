package it.stamp.main.core.di

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.navigation3.ui.NavDisplay
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
        entry<MainNavKey>(
            metadata = NavDisplay.transitionSpec {
                EnterTransition.None togetherWith fadeOut(tween(1000))
            } + NavDisplay.popTransitionSpec {
                EnterTransition.None togetherWith ExitTransition.None
            } + NavDisplay.predictivePopTransitionSpec {
                EnterTransition.None togetherWith ExitTransition.None
            },
        ) {
            MainScreen(navigator)
        }
    }
}