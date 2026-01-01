package it.stamp.mypage.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.mypage.MyPageNavKey
import it.stamp.mypage.impl.MyPageScreen

fun EntryProviderScope<NavKey>.myPageEntry() {
    entry<MyPageNavKey> {
        MyPageScreen()
    }
}