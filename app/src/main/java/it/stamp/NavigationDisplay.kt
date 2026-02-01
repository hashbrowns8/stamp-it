package it.stamp

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

@Composable
fun StampNavigationDisplay(
    backStack: List<NavKey>,
    modifier: Modifier = Modifier,
    entryProvider: (key: NavKey) -> NavEntry<NavKey>,
) {
    val animationSpec = remember {
        tween<IntOffset>(durationMillis = 500)
    }

    NavDisplay(
        backStack,
        modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            ContentTransform(
                slideInHorizontally(animationSpec) { it },
                slideOutHorizontally(animationSpec),
            )
        },
        popTransitionSpec = {
            ContentTransform(
                slideInHorizontally(animationSpec),
                slideOutHorizontally(animationSpec) { it },
            )
        },
        predictivePopTransitionSpec = {
            ContentTransform(
                slideInHorizontally(animationSpec),
                slideOutHorizontally(animationSpec) { it },
            )
        },
        entryProvider = entryProvider,
    )
}