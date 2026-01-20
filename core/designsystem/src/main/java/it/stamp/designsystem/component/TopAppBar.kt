package it.stamp.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White

@Composable
fun StampTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(colors.containerColor)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        navigationIcon
            ?.invoke()
            ?: Spacer(Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart,
        ) {
            ProvideTextStyle(
                value = MaterialTheme.typography.titleMedium,
                content = title,
            )
        }

        if (actions != null) {
            CompositionLocalProvider(LocalContentColor provides colors.actionIconContentColor) {
                Row(content = actions)
            }
        }
    }
}

object TopAppBarDefaults {

    fun topAppBarColors(
        containerColor: Color = White,
        navigationIconContentColor: Color = Gray800,
        titleContentColor: Color = Gray800,
        actionIconContentColor: Color = Gray500,
    ): TopAppBarColors = TopAppBarColors(
        containerColor,
        scrolledContainerColor = Color.Unspecified,
        navigationIconContentColor,
        titleContentColor,
        actionIconContentColor,
        subtitleContentColor = Color.Unspecified,
    )
}

@Preview
@Composable
private fun StampTopAppBarPreview() {
    StampTheme {
        StampTopAppBar(
            title = {
                Text("미션 전달하기")
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                    }
                ) {
                    Icon(
                        imageVector = Drawables.ArrowLeft,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                    }
                ) {
                    Icon(
                        imageVector = Drawables.Plus,
                        contentDescription = null,
                    )
                }
            },
        )
    }
}