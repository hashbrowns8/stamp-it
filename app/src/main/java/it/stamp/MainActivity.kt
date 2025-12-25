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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import it.stamp.designsystem.component.StampSnackbar
import it.stamp.designsystem.component.StampSnackbarVisuals
import it.stamp.designsystem.theme.StampTheme
import it.stamp.main.navigation.Main
import it.stamp.main.navigation.mainScreenEntry
import it.stamp.signin.navigation.SignIn
import it.stamp.signin.navigation.signInScreenEntry
import it.stamp.splash.SplashScreen
import it.stamp.ui.LocalMembership
import it.stamp.ui.LocalSnackbarHostState
import it.stamp.ui.LocalUser
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen() // TODO : Vector Drawable 로 변경
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StampTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                splashScreen.setKeepOnScreenCondition {
                    uiState == MainActivityUiState.Loading
                }

                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            snackbar = { snackbarData ->
                                StampSnackbar(snackbarData)
                            },
                        )
                    },
                ) { _ : PaddingValues ->
                    when (val uiState = uiState) {
                        MainActivityUiState.Loading -> SplashScreen()
                        is MainActivityUiState.Success -> with(uiState) {
                            CompositionLocalProvider(
                                LocalUser provides user,
                                LocalMembership provides membership,
                                LocalSnackbarHostState provides snackbarHostState,
                            ) {
                                StampNaivagionDisplay(
                                    startDestination = if (user == null) {
                                        SignIn
                                    } else {
                                        Main
                                    },
                                )
                            }
                        }
                        is MainActivityUiState.Failure -> with(uiState) {
                            val coroutineScope = rememberCoroutineScope()

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(StampSnackbarVisuals.Default(message))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T : NavKey> StampNaivagionDisplay(
    startDestination: T,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(startDestination)

    NavDisplay(
        backStack,
        modifier,
        onBack = {
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            signInScreenEntry(
                onSignInSuccess = {
                    backStack.clear(); backStack.add(Main)
                },
            )

            mainScreenEntry()
        },
    )
}