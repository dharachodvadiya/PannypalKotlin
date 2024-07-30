package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()){

        Image(painter = painterResource(id = R.drawable.icon_loading), contentDescription = "icon")
Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding)))
        Text(
            text = stringResource(id = R.string.loading),
            style = MyAppTheme.typography.Semibold60,
            color = MyAppTheme.colors.black
        )
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    PannyPalTheme {
        LoadingScreen()
    }
}