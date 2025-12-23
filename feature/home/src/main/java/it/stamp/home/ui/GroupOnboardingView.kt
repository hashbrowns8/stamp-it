package it.stamp.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.component.ButtonSize
import it.stamp.designsystem.component.PrimaryButton
import it.stamp.designsystem.icon.CharacterRed
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red50
import it.stamp.designsystem.theme.StampItTheme
import it.stamp.designsystem.theme.White
import it.stamp.home.R

@Composable
fun GroupOnboardingView(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Red50, RoundedCornerShape(20.dp))
            .padding(vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = Drawables.CharacterRed,
            contentDescription = null,
        )

        Text(
            stringResource(R.string.group_onboarding_message),
            color = Gray800,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )

        PrimaryButton(onClick, size = ButtonSize.Medium) {
            Text(stringResource(R.string.group_onboarding_action))
        }
    }
}

@Preview
@Composable
private fun GroupOnboardingViewPreview() {
    StampItTheme {
        GroupOnboardingView(
            onClick = {
            },
            modifier = Modifier
                .background(White)
                .padding(16.dp)
        )
    }
}