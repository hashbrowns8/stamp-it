package it.stamp.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.navigation3.runtime.rememberNavBackStack
import it.stamp.designsystem.component.StampSnackbar
import it.stamp.designsystem.component.displaySnackbar
import it.stamp.designsystem.theme.White
import it.stamp.home.navigation.HomeNavKey
import it.stamp.missions.navigation.MissionsNavKey
import it.stamp.mypage.navigation.MyNavKey
import kotlinx.coroutines.launch

@Composable
internal fun MainScreen(
    onInviteGroupClick: () -> Unit,
    modifier: Modifier = Modifier,
    destinations: List<TopLevelDestination> = TopLevelDestination.all,
) {
    val backStack = rememberNavBackStack(
        MyNavKey,
        MissionsNavKey,
        HomeNavKey
    )

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val resources = LocalResources.current

    val coroutineScope = rememberCoroutineScope()

    val onShowErrorSnackbar: (Throwable) -> Unit = { throwable ->
        coroutineScope.launch {
            when (throwable) { // TODO
                else -> resources.getString(R.string.unknown_error_message)
            }.let { message ->
                snackbarHostState.displaySnackbar(message)
            }
        }
    }

    Scaffold(
        modifier,
        bottomBar = {
            MainNavigationBar(
                currentNavigationKey = backStack.last(),
                onTabClick = { navigationKey ->
                    backStack.remove(navigationKey)
                    backStack.add(navigationKey)
                },
                destinations = destinations,
            )
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                snackbar = {
                    StampSnackbar(it)
                },
            )
        },
        containerColor = White,
    ) { _ : PaddingValues ->
        MainNavigationDisplay(
            backStack,
            onBack = {
                backStack.remove(HomeNavKey)
                backStack.add(HomeNavKey)
            },
            onNotificationsClick = {},
            onInviteGroupClick = onInviteGroupClick,
            onJoinGroupClick = {},
            onViewMyMissionsMoreClick = {},
            onViewMembersMissionsMoreClick = {},
            onShowErrorSnackbar,
        )
    }
}