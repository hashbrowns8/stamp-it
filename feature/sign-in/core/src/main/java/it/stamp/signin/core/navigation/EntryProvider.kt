package it.stamp.signin.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.signin.SignInNavKey
import it.stamp.signin.core.SignInScreen

fun EntryProviderScope<NavKey>.signInEntry(onSignInSuccess: () -> Unit) {
    entry<SignInNavKey> {
        SignInScreen(onSignInSuccess)
    }
}