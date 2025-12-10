package it.stamp.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import it.stamp.data.source.GoogleCredentialDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideGoogleCredentialDataSource(
        @ActivityContext context: Context
    ): GoogleCredentialDataSource = GoogleCredentialDataSource(context)
}