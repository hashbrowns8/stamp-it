package it.stamp.main.core

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
import it.stamp.home.core.navigation.homeScreenEntry
import it.stamp.missions.core.navigation.missionsScreenEntry
import it.stamp.mypage.core.navigation.myPageEntry
import it.stamp.navigation.Navigator

@Composable
fun MainNavigationDisplay(
    backStack: List<NavKey>,
    onBack: () -> Unit,
    navigator: Navigator,
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
            homeScreenEntry(navigator)

            missionsScreenEntry()

            myPageEntry()
        },
    )
}