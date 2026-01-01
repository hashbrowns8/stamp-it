package it.stamp.mypage.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.mypage.MyPageNavKey
import it.stamp.mypage.core.MyPageScreen

fun EntryProviderScope<NavKey>.myPageEntry() {
    entry<MyPageNavKey> {
        MyPageScreen()
    }
}