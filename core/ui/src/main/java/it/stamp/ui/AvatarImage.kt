package it.stamp.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import it.stamp.designsystem.icon.Drawables
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