package com.indie.apps.pennypal.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
    ) {

        //Image(painter = painterResource(id = R.drawable.icon_loading), contentDescription = "icon")
        //Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding)))
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
    PennyPalTheme(darkTheme = true) {
        LoadingScreen()
    }
}