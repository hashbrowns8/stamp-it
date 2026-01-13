package it.stamp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import dagger.hilt.android.AndroidEntryPoint
import it.stamp.designsystem.component.StampSnackbar
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.main.MainNavKey
import it.stamp.model.authentication.AuthState
import it.stamp.navigation.EntryProviderInstaller
import it.stamp.navigation.Navigator
import it.stamp.signin.SignInNavKey
import it.stamp.ui.LocalSnackbarHostState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryProviderScopes: Set<@JvmSuppressWildcards EntryProviderInstaller>

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen() // TODO : Vector Drawable 로 변경
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StampTheme {
                val authenticationState by viewModel.authState.collectAsStateWithLifecycle()

                splashScreen.setKeepOnScreenCondition { // TODO : 최대 로딩 시간 기다리고 실패
                    authenticationState == AuthState.Unknown
                }

                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            snackbar = { snackbarData ->
                                StampSnackbar(snackbarData)
                            },
                        )
                    },
                    containerColor = White,
                ) { innerPadding ->
                    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                        if (authenticationState == AuthState.Unknown) return@CompositionLocalProvider

                        val startDestination = if (authenticationState is AuthState.Authenticated) {
                            MainNavKey
                        } else {
                            SignInNavKey
                        }

                        navigator.setStartDestination(startDestination)

                        StampNavigationDisplay(
                            backStack = navigator.backStack,
                            modifier = Modifier
                                .padding(innerPadding)
                                .consumeWindowInsets(innerPadding),
                            entryProvider = entryProvider {
                                entryProviderScopes.forEach { builder ->
                                    this.builder()
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}