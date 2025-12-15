package it.stamp.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

fun EntryProviderScope<NavKey>.homeScreenEntry() {
    entry<Home> {
        HomeScreen()
    }
}