package it.stamp.mypage.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.invite.group.InviteGroupNavKey
import it.stamp.join.group.api.JoinGroupNavKey
import it.stamp.mypage.MyPageNavKey
import it.stamp.mypage.core.MyPageScreen
import it.stamp.navigation.Navigator

fun EntryProviderScope<NavKey>.myPageEntry(navigator: Navigator) {
    entry<MyPageNavKey> {
        MyPageScreen(
            navigateToEditProfile = {
            },
            navigateToManageMembers = {
            },
            navigateToInviteMember = {
                navigator.navigate(InviteGroupNavKey)
            },
            navigateToJoinGroup = {
                navigator.navigate(JoinGroupNavKey)
            },
        )
    }
}