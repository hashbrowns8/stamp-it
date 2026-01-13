package it.stamp.home.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.home.HomeNavKey
import it.stamp.home.core.HomeScreen
import it.stamp.invite.group.InviteGroupNavKey
import it.stamp.join.group.api.JoinGroupNavKey
import it.stamp.navigation.Navigator

fun EntryProviderScope<NavKey>.homeScreenEntry(navigator: Navigator) {
    entry<HomeNavKey> {
        HomeScreen(
            onNotificationsClick = {},
            onInviteGroupClick = {
                navigator.navigate(InviteGroupNavKey)
            },
            onJoinGroupClick = {
                navigator.navigate(JoinGroupNavKey)
            },
            onViewMyMissionsMoreClick = {},
            onViewMembersMissionsMoreClick = {},
        )
    }
}