package it.stamp.signin.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.signin.SignInScreen

fun EntryProviderScope<NavKey>.signInScreenEntry(onSignInSuccess: () -> Unit) {
    entry<SignIn> {
        SignInScreen(onSignInSuccess)
    }
}