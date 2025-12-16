package it.stamp.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import it.stamp.designsystem.R

object Icons

val Icons.Cancel: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.icon_cancel)

val Icons.Logo: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo)

val Icons.LogoInversed: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo_inversed)

val Icons.LogoOutlined: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo_outlined)

val Icons.ArrowLeft: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.icon_arrow_left)

val Icons.Plus: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.icon_plus)

val Icons.FirstRank: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.rank_first)

val Icons.SecondRank: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.rank_second)

val Icons.ThirdRank: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.rank_third)

val Icons.ChevronRight: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.icon_chevron_right)

val MissionChore: Painter
    @Composable
    get() = painterResource(R.drawable.mission_chore)

val MissionCommunication: Painter
    @Composable
    get() = painterResource(R.drawable.mission_communication)

val MissionCustom: Painter
    @Composable
    get() = painterResource(R.drawable.mission_custom)

val MissionHealth: Painter
    @Composable
    get() = painterResource(R.drawable.mission_health)

val MissionLearning: Painter
    @Composable
    get() = painterResource(R.drawable.mission_learning)