package it.stamp.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeNavKey : NavKey

fun EntryProviderScope<NavKey>.homeScreenEntry(
    onNotificationsClick: () -> Unit,
    onGroupOnboardingClick: () -> Unit,
    onViewMyMissionsMoreClick: () -> Unit,
    onViewMembersMissionsMoreClick: () -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
) {
    entry<HomeNavKey> {
        HomeScreen(
            onNotificationsClick,
            onGroupOnboardingClick,
            onViewMyMissionsMoreClick,
            onViewMembersMissionsMoreClick,
            onShowErrorSnackbar,
        )
    }
}