package it.stamp.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import it.stamp.home.navigation.homeScreenEntry
import it.stamp.missions.navigation.missionsScreenEntry
import it.stamp.mypage.navigation.myScreenEntry

@Composable
fun MainNavigationDisplay(
    backStack: List<NavKey>,
    onShowErrorSnackbar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack,
        modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            homeScreenEntry( // TODO : Navigation
                onNotificationsClick = {},
                onInviteGroupClick = {},
                onJoinGroupClick = {},
                onViewMyMissionsMoreClick = {},
                onViewMembersMissionsMoreClick = {},
                onShowErrorSnackbar = onShowErrorSnackbar,
            )

            missionsScreenEntry()

            myScreenEntry()
        },
    )
}