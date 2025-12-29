package it.stamp.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.rememberNavBackStack
import it.stamp.designsystem.component.StampSnackbar
import it.stamp.designsystem.component.showStampSnackbar
import it.stamp.designsystem.theme.White
import it.stamp.home.navigation.HomeNavKey
import it.stamp.missions.navigation.MissionsNavKey
import it.stamp.mypage.navigation.MyNavKey
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
internal fun MainScreen(
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

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val onShowErrorSnackbar: (Throwable) -> Unit = { throwable ->
        coroutineScope.launch {
            when (throwable) { // TODO
                else -> context.getString(R.string.unknown_error_message)
            }.let { message ->
                Timber.d("show snackbar ..")
                snackbarHostState.showStampSnackbar(message)
            }
        }
    }

    Scaffold(
        modifier,
        bottomBar = {
            MainNavigationBar(
                currentNavigationKey = backStack.last(),
                onTabClick = {
                    backStack.remove(it)
                    backStack.add(it)
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
    ) { paddingValues ->
        MainNavigationDisplay(
            backStack,
            onShowErrorSnackbar,
            modifier = Modifier.padding(paddingValues)
        )
    }
}