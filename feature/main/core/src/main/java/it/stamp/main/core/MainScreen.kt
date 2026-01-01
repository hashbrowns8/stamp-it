package it.stamp.main.core

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import it.stamp.designsystem.component.StampSnackbar
import it.stamp.designsystem.theme.White
import it.stamp.home.HomeNavKey
import it.stamp.missions.MissionsNavKey
import it.stamp.mypage.MyPageNavKey
import it.stamp.navigation.Navigator
import it.stamp.ui.LocalSnackbarHostState

@Composable
internal fun MainScreen(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    destinations: List<TopLevelDestination> = TopLevelDestination.all,
) {
    val backStack = rememberNavBackStack(
        MyPageNavKey,
        MissionsNavKey,
        HomeNavKey
    )

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val currentTab by remember {
        derivedStateOf {
            backStack
                .last()
                .let { topMost ->
                    destinations.first { it.navigationKey == topMost }
                }
        }
    }

    Scaffold(
        modifier,
        bottomBar = {
            MainNavigationBar(
                currentTab = currentTab,
                onTabClick = { destination ->
                    backStack.remove(destination.navigationKey)
                    backStack.add(destination.navigationKey)
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
    ) { innerPadding ->
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            MainNavigationDisplay(
                backStack,
                onBack = {
                    backStack.remove(HomeNavKey)
                    backStack.add(HomeNavKey)
                },
                navigator,
                modifier = Modifier.consumeWindowInsets(innerPadding),
            )
        }
    }
}