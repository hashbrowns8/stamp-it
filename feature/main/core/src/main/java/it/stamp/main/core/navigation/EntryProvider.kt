package it.stamp.main.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.main.MainNavKey
import it.stamp.main.core.MainScreen
import it.stamp.navigation.Navigator

fun EntryProviderScope<NavKey>.mainEntry(navigator: Navigator) {
    entry<MainNavKey> {
        MainScreen(navigator)
    }
}