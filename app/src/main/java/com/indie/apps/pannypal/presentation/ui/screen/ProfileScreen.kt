package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pannypal.presentation.ui.component.screen.ProfileSection2
import com.indie.apps.pannypal.presentation.ui.component.screen.ProfileTopSection
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun ProfileScreen(
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO setProfile data
    Scaffold(
        topBar = {
            TopBarWithTitle(
                title = stringResource(id = R.string.profile),
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.brandBg
            )
        }
    ) { padding ->

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(padding)
        ) {
            ProfileTopSection(Modifier.height(screenHeight * 0.3f))
            ProfileSection2(
                onLoginWithGoogle = {})
        }

    }
}



@Preview
@Composable
private fun ProfileScreenPreview() {
    PannyPalTheme {
        ProfileScreen({})
    }
}