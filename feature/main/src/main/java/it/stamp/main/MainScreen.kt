package it.stamp.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import it.stamp.designsystem.theme.White
import it.stamp.home.navigation.Home
import it.stamp.home.navigation.homeScreenEntry

@Composable
internal fun MainScreen(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(Home)

    Scaffold(
        modifier,
        containerColor = White,
    ) { paddingValues ->
        NavDisplay(
            backStack,
            modifier = Modifier.padding(paddingValues),
            entryProvider = entryProvider {
                homeScreenEntry()
            },
        )
    }
}