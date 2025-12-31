package it.stamp.main

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
    onBack: () -> Unit,
    onNotificationsClick: () -> Unit,
    onInviteGroupClick: () -> Unit,
    onJoinGroupClick: () -> Unit,
    onViewMyMissionsMoreClick: () -> Unit,
    onViewMembersMissionsMoreClick: () -> Unit,
    onShowErrorSnackbar: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack,
        modifier,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            ContentTransform(
                EnterTransition.None,
                ExitTransition.None,
            )
        },
        popTransitionSpec = {
            ContentTransform(
                EnterTransition.None,
                ExitTransition.None,
            )
        },
        predictivePopTransitionSpec = {
            ContentTransform(
                EnterTransition.None,
                ExitTransition.None,
            )
        },
        entryProvider = entryProvider {
            homeScreenEntry(
                onNotificationsClick = {},
                onInviteGroupClick = onInviteGroupClick,
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