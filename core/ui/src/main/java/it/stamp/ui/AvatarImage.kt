package it.stamp.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Blue400
import it.stamp.designsystem.theme.Purple400
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.Yellow400
import it.stamp.model.user.Avatar

@Composable
fun AvatarImage(
    avatar: Avatar,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Image(
        avatar.drawable,
        contentDescription = null,
        modifier,
        contentScale = contentScale,
    )
}

val Avatar.drawable: ImageVector
    @Composable
    get() = when (this) {
        Avatar.CHARACTER_1 -> Drawables.CharacterRed
        Avatar.CHARACTER_2 -> Drawables.CharacterBlue
        Avatar.CHARACTER_3 -> Drawables.CharacterYellow
        Avatar.CHARACTER_4 -> Drawables.CharacterPurple
        // TODO : Grape
        Avatar.CHARACTER_5 -> Drawables.CharacterRed
        Avatar.CHARACTER_6 -> Drawables.CharacterBlue
        Avatar.CHARACTER_7 -> Drawables.CharacterYellow
        Avatar.CHARACTER_8 -> Drawables.CharacterPurple
    }

val Avatar.color: Color
    get() = when (this) {
        Avatar.CHARACTER_1 -> Red400
        Avatar.CHARACTER_2 -> Blue400
        Avatar.CHARACTER_3 -> Yellow400
        Avatar.CHARACTER_4 -> Purple400
        Avatar.CHARACTER_5 -> Red400
        Avatar.CHARACTER_6 -> Blue400
        Avatar.CHARACTER_7 -> Yellow400
        Avatar.CHARACTER_8 -> Purple400
    }