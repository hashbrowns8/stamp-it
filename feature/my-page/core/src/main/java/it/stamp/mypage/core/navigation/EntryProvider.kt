package it.stamp.mypage.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.edit.profile.EditProfileNavKey
import it.stamp.invite.member.InviteMemberNavKey
import it.stamp.join.group.api.JoinGroupNavKey
import it.stamp.membership.MembershipNavKey
import it.stamp.mypage.MyPageNavKey
import it.stamp.mypage.core.MyPageScreen
import it.stamp.navigation.Navigator

fun EntryProviderScope<NavKey>.myPageEntry(navigator: Navigator) {
    entry<MyPageNavKey> {
        MyPageScreen(
            navigateToEditProfile = {
                navigator.navigate(EditProfileNavKey)
            },
            navigateToManageMembers = {
                navigator.navigate(MembershipNavKey)
            },
            navigateToInviteMember = {
                navigator.navigate(InviteMemberNavKey)
            },
            navigateToJoinGroup = {
                navigator.navigate(JoinGroupNavKey)
            },
        )
    }
}