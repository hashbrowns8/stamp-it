package it.stamp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class StampItApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(
            object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "hashbrowns8good"
                }
            }
        )
    }
}