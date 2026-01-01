package it.stamp

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import it.stamp.navigation.Navigator

@Composable
fun StampNaivagionDisplay(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    entryProvider: (key: NavKey) -> NavEntry<NavKey>,
) {
    NavDisplay(
        navigator.backStack,
        modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            ContentTransform(
                slideInHorizontally { it },
                slideOutHorizontally(),
            )
        },
        popTransitionSpec = {
            ContentTransform(
                slideInHorizontally(),
                slideOutHorizontally { it },
            )
        },
        predictivePopTransitionSpec = {
            ContentTransform(
                slideInHorizontally(),
                slideOutHorizontally { it },
            )
        },
        entryProvider = entryProvider,
    )
}