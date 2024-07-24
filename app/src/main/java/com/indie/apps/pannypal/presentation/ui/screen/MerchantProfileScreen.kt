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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantProfileBottomSection
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantProfileTopSection
import com.indie.apps.pannypal.presentation.ui.component.screen.ProfileSection2
import com.indie.apps.pannypal.presentation.ui.component.screen.ProfileTopSection
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun  MerchantProfileScreen(
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier) {

    // TODO setProfile data
    Scaffold(
        topBar = {
            TopBarWithTitle(
                title = "",
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.white
            )
        }
    ) { padding ->

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(padding)
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        ) {
            MerchantProfileTopSection(Modifier.height(screenHeight * 0.35f))
            MerchantProfileBottomSection()
        }

    }

}

@Preview
@Composable
private fun MerchantProfileScreenPreview() {
    PannyPalTheme {
        MerchantProfileScreen({})
    }
}