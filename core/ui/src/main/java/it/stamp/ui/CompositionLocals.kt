package it.stamp.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import it.stamp.model.membership.Membership
import it.stamp.model.user.User

val LocalUser = compositionLocalOf<User?> {
    null
}

val LocalMembership = compositionLocalOf<Membership?> {
    null
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState Not Provided :-/")
}