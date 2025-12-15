package it.stamp.main.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.main.MainScreen

fun EntryProviderScope<NavKey>.mainScreenEntry() {
    entry<MainNavigationKey> {
        MainScreen()
    }
}