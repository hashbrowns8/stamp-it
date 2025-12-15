package it.stamp.signin.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.signin.SignInScreen
import kotlinx.serialization.Serializable

@Serializable
object SignIn : NavKey

fun EntryProviderScope<NavKey>.signInScreenEntry(onSignInSuccess: () -> Unit) {
    entry<SignIn> {
        SignInScreen(onSignInSuccess)
    }
}