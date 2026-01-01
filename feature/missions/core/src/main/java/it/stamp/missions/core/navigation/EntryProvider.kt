package it.stamp.missions.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import it.stamp.missions.MissionsNavKey
import it.stamp.missions.core.MissionsScreen

fun EntryProviderScope<NavKey>.missionsScreenEntry() {
    entry<MissionsNavKey> {
        MissionsScreen()
    }
}