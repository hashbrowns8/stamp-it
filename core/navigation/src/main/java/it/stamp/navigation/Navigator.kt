package it.stamp.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.scopes.ActivityRetainedScoped

// TODO
@ActivityRetainedScoped
class Navigator {
    val backStack: SnapshotStateList<NavKey> = mutableStateListOf()

    fun setStartDestination(key: NavKey) {
        if (backStack.isEmpty()) backStack.add(key)
    }

    fun navigate(key: NavKey) {
        backStack.add(key)
    }

    fun navigateBack() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }
}

typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit