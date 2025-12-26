package it.stamp.mypage.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.mypage.MyScreen
import kotlinx.serialization.Serializable

@Serializable
data object MyNavKey : NavKey

fun EntryProviderScope<NavKey>.myScreenEntry() {
    entry<MyNavKey> {
        MyScreen()
    }
}