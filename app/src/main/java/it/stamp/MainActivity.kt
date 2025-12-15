package it.stamp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import it.stamp.designsystem.theme.StampItTheme
import it.stamp.main.navigation.Main
import it.stamp.main.navigation.mainScreenEntry
import it.stamp.signin.navigation.SignIn
import it.stamp.signin.navigation.signInScreenEntry

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StampItTheme {
                val backStack = rememberNavBackStack(SignIn)
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