package it.stamp

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import it.stamp.invite.group.core.navigation.inviteGroupEntry
import it.stamp.main.MainNavKey
import it.stamp.main.core.navigation.mainEntry
import it.stamp.navigation.Navigator
import it.stamp.signin.core.navigation.signInEntry

@Composable
fun <T : NavKey> StampNaivagionDisplay(
    startDestination: T,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(startDestination)

    val navigator = remember(startDestination) {
        Navigator(backStack)
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
        entryProvider = entryProvider {
            signInEntry(
                onSignInSuccess = {
                    navigator.navigateBack()
                    navigator.navigate(MainNavKey)
                },
            )

            mainEntry(navigator)

            inviteGroupEntry(
                onBackClick = {
                    navigator.navigateBack()
                }
            )
        },
    )
}