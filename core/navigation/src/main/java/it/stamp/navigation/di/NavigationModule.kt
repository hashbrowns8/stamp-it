package it.stamp.navigation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import it.stamp.navigation.Navigator

@Module
@InstallIn(ActivityRetainedComponent::class)
object NavigationModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNavigator(): Navigator = Navigator()
}