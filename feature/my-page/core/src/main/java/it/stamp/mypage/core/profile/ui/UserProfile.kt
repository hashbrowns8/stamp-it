package it.stamp.mypage.core.profile.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray100
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.model.user.Avatar
import it.stamp.ui.AvatarImage

@Composable
fun UserProfile(
    avatar: Avatar,
    groupName: String,
    displayName: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable(onClick = onEditClick),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .border(1.dp, Gray50, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            AvatarImage(
                avatar,
                modifier = Modifier.width(40.dp),
            )
        }

        Spacer(Modifier)

        Text(
            groupName,
            color = Gray500,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.labelSmall,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.width(18.dp))

            Text(
                text = displayName,
                color = Gray800,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
            )

            Icon(
                imageVector = Drawables.Edit,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Gray100,
            )
        }
    }
}