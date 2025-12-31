package it.stamp.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.icon.LogoInversed
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.White

// TODO : SplashScreen 제거
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Red400),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(Drawables.LogoInversed, contentDescription = null)

            Text(
                text = stringResource(R.string.tagline),
                color = White,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}