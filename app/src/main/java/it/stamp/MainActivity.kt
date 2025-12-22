package it.stamp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import it.stamp.designsystem.theme.StampItTheme
import it.stamp.main.navigation.Main
import it.stamp.main.navigation.mainScreenEntry
import it.stamp.model.membership.Membership
import it.stamp.model.user.User
import it.stamp.signin.navigation.signInScreenEntry
import it.stamp.ui.LocalMembership
import it.stamp.ui.LocalSnackbarHostState
import it.stamp.ui.LocalUser

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StampItTheme {
                val me: User? by viewModel.me.collectAsStateWithLifecycle()

                val membership: Membership? by viewModel.membership.collectAsStateWithLifecycle()

                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                CompositionLocalProvider(
                    LocalUser provides me,
                    LocalMembership provides membership,
                    LocalSnackbarHostState provides snackbarHostState,
                ) {
                    val backStack = rememberNavBackStack(Main)

                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(snackbarHostState)
                        },
                    ) { _ : PaddingValues ->
                        NavDisplay(
                            backStack = backStack,
                            onBack = {
                            },
                            entryDecorators = listOf(
                                rememberSaveableStateHolderNavEntryDecorator(),
                                rememberViewModelStoreNavEntryDecorator(),
                            ),
                            entryProvider = entryProvider {
                                signInScreenEntry(
                                    onSignInSuccess = {
                                        backStack.clear()
                                        backStack.add(Main)
                                    },
                                )

                                mainScreenEntry()
                            },
                        )
                    }
                }
            }
        }
    }
}