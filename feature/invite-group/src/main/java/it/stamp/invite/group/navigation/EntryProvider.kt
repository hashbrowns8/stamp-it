package it.stamp.invite.group.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.invite.group.InviteGroupScreen
import kotlinx.serialization.Serializable

@Serializable
data object InviteGroupNavKey : NavKey

fun EntryProviderScope<NavKey>.inviteGroupEntry(
    onBackClick: () -> Unit,
) {
    entry<InviteGroupNavKey> {
        InviteGroupScreen(onBackClick)
    }
}