package it.stamp.main.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.main.MainScreen
import kotlinx.serialization.Serializable

@Serializable
data object MainNavKey : NavKey

fun EntryProviderScope<NavKey>.mainEntry(
    navigateToInviteGroup: () -> Unit,
) {
    entry<MainNavKey> {
        MainScreen(onInviteGroupClick = navigateToInviteGroup)
    }
}