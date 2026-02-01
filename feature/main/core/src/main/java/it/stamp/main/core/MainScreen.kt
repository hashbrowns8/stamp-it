package it.stamp.main.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import it.stamp.designsystem.component.StampSnackbar
import it.stamp.home.HomeNavKey
import it.stamp.navigation.Navigator
import it.stamp.ui.LocalSnackbarHostState

@Composable
internal fun MainScreen(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    destinations: List<TopLevelDestination> = TopLevelDestination.all,
) {
    val backStack = rememberNavBackStack(HomeNavKey)

    val snackbarHostState = remember { SnackbarHostState() }

    val currentTab by remember {
        derivedStateOf {
            backStack
                .last()
                .let { topMost ->
                    destinations.first { it.navigationKey == topMost }
                }
        }
    }

    Column(modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                MainNavigationDisplay(
                    backStack,
                    onBack = {
                        if (backStack.last() != HomeNavKey) {
                            backStack.removeLastOrNull()
                            backStack.add(HomeNavKey)
                        }
                    },
                    navigator,
                )
            }

            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
                snackbar = {
                    StampSnackbar(it)
                },
            )
        }

        MainNavigationBar(
            currentTab,
            onTabClick = { destination ->
                destination.navigationKey
                    .let { navigationKey ->
                        backStack.remove(navigationKey)
                        backStack.add(navigationKey)
                    }
            },
            destinations = destinations,
        )
    }
}