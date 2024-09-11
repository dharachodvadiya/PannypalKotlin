package com.indie.apps.pennypal.presentation.ui.screen.loading

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun LoadingWithProgress(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxSize().background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        //CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        LoadingWithProgress()
    }
}