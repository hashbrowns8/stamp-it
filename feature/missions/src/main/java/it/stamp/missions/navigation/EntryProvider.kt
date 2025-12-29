package it.stamp.missions.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.missions.MissionsScreen
import kotlinx.serialization.Serializable

@Serializable
data object MissionsNavKey : NavKey

fun EntryProviderScope<NavKey>.missionsScreenEntry() {
    entry<MissionsNavKey> {
        MissionsScreen()
    }
}