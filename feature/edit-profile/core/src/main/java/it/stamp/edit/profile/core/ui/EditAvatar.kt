package it.stamp.edit.profile.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.White
import it.stamp.edit.profile.core.R
import it.stamp.model.user.Avatar
import it.stamp.ui.AvatarImage
import it.stamp.ui.color

@Composable
fun EditAvatar(
    avatar: Avatar,
    onAvatarChange: (Avatar) -> Unit,
    modifier: Modifier = Modifier,
    avatars: List<Avatar> = Avatar.entries,
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Label(
            text = stringResource(R.string.avatar),
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(avatars.size) { index ->
                with(avatars[index]) {
                    val selected = avatar == this

                    val (backgroundColor, borderColor) = if (selected) {
                        White to avatar.color
                    } else {
                        Gray25 to Gray50
                    }

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(backgroundColor)
                            .border(1.dp, borderColor, CircleShape)
                            .clickable {
                                onAvatarChange(this)
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        AvatarImage(
                            avatar = this@with,
                            modifier = Modifier.width(32.dp),
                        )
                    }
                }
            }
        }
    }
}