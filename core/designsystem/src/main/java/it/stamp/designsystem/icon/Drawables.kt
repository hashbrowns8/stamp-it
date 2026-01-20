package it.stamp.designsystem.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import it.stamp.designsystem.R
import it.stamp.designsystem.theme.StampTheme

object Drawables {
    val ArrowLeft: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_arrow_left)

    val Bell: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_bell)

    val Cancel: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_cancel)

    val Check: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_check)

    val CheckCircle: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_check_circle)

    val ChevronRight: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_chevron_right)

    val Edit: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_edit)

    val Export: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_export)

    val Home: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_home)

    val HomeFilled: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_home_filled)

    val Flag: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_flag)

    val FlagFilled: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_flag_filled)

    val MyPage: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_mypage)

    val MyPageFilled: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_mypage_filled)

    val Plus: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_plus)

    val X: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.icon_x)

    val Logo: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.logo)

    val LogoInversed: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.logo_inversed)

    val LogoOutlined: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.logo_outlined)

    val FirstRank: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.rank_first)

    val SecondRank: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.rank_second)

    val ThirdRank: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.rank_third)

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

    val CharacterBlue: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.character_blue)

    val CharacterPurple: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.character_purple)

    val CharacterRed: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.character_red)

    val CharacterRedSad: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.character_red_sad)

    val CharacterRedSadMono: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.character_red_sad_mono)

    val CharacterWhite: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.character_white)

    val CharacterYellow: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.character_yellow)
}

@Preview
@Composable
private fun IconsPreview() {
    StampTheme {
        LazyVerticalGrid(GridCells.Fixed(5)) {
            item {
                Image(
                    Drawables.Cancel,
                    contentDescription = null
                )
            }
        }
    }
}