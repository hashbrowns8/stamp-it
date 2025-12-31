package it.stamp.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation3.runtime.NavKey
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.White

@Composable
fun MainNavigationBar(
    currentNavigationKey: NavKey,
    onTabClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    destinations: List<TopLevelDestination> = TopLevelDestination.all,
) {
    Box(modifier) {
        Row(
            modifier = Modifier
                .background(White)
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 16.dp),
        ) {
            destinations.forEach { destination ->
                val selected = destination.navigationKey == currentNavigationKey

                val color = if (selected) {
                    Red400
                } else {
                    Gray500
                }
                Column(
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxHeight()
                        .clickable {
                            onTabClick(destination.navigationKey)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CompositionLocalProvider(LocalContentColor provides color) {
                        Icon(
                            imageVector = destination.icon(selected),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )

                        val context = LocalContext.current

                        when (destination) {
                            Home -> context.getString(R.string.home)
                            Missions -> context.getString(R.string.mission)
                            My -> context.getString(R.string.my)
                        }.let { label ->
                            Text(
                                label,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 1.em,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = Gray25)
    }
}