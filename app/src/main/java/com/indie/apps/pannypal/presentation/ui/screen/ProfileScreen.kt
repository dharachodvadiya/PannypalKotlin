package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.MyAppTopBar
import com.indie.apps.pannypal.presentation.ui.component.UserProfile
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.component.screen.AmountWithIcon
import com.indie.apps.pannypal.presentation.ui.component.screen.LoginWithGoogleButton
import com.indie.apps.pannypal.presentation.ui.component.screen.UserProfileSection1
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
            MyAppTopBar(
                title = "Profile",
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.brandBg
            )
        }
    ) { padding ->

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(padding)
        ) {
            UserProfileSection1(Modifier.height(screenHeight * 0.3f))
            UserProfileSection2({})
        }

    }
}

@Composable
fun UserProfileSection2(
    onLoginWithGoogle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            AmountWithIcon(
                amount = 5.0,
                isPositive = true
            )
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(70.dp)
                    .background(MyAppTheme.colors.gray1)
            )
            AmountWithIcon(
                amount = -5.0,
                isPositive = false
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        LoginWithGoogleButton(
            onClick = onLoginWithGoogle,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding))
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.button_height))
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    PannyPalTheme {
        ProfileScreen({})
    }
}