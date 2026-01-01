package it.stamp.invite.group.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.invite.group.InviteGroupNavKey
import it.stamp.invite.group.core.InviteGroupScreen

fun EntryProviderScope<NavKey>.inviteGroupEntry(
    onBackClick: () -> Unit,
) {
    entry<InviteGroupNavKey> {
        InviteGroupScreen(onBackClick)
    }
}