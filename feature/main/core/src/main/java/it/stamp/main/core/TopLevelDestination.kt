package it.stamp.main.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.icon.Flag
import it.stamp.designsystem.icon.FlagFilled
import it.stamp.designsystem.icon.Home
import it.stamp.designsystem.icon.HomeFilled
import it.stamp.designsystem.icon.MyPage
import it.stamp.designsystem.icon.MyPageFilled
import it.stamp.home.HomeNavKey
import it.stamp.missions.MissionsNavKey
import it.stamp.mypage.MyPageNavKey

sealed interface TopLevelDestination {
    @Composable
    fun icon(selected: Boolean): ImageVector

    val navigationKey: NavKey

    companion object {
        val all = listOf(Home, Missions, My)
    }
}

data object Home : TopLevelDestination {
    @Composable
    override fun icon(selected: Boolean): ImageVector = if (selected) {
        Drawables.HomeFilled
    } else {
        Drawables.Home
    }

    override val navigationKey: NavKey = HomeNavKey
}

data object Missions : TopLevelDestination {
    @Composable
    override fun icon(selected: Boolean): ImageVector = if (selected) {
        Drawables.FlagFilled
    } else {
        Drawables.Flag
    }

    override val navigationKey: NavKey = MissionsNavKey
}

data object My : TopLevelDestination {
    @Composable
    override fun icon(selected: Boolean): ImageVector = if (selected) {
        Drawables.MyPageFilled
    } else {
        Drawables.MyPage
    }

    override val navigationKey: NavKey = MyPageNavKey
}