package com.indie.apps.pennypal.presentation.ui.component.composable.common

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.TopBar
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun TopBarWithTitle(
    isBackEnable: Boolean = true,
    title: String,
    titleStyle: TextStyle = MyAppTheme.typography.Semibold57,
    onNavigationUp: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.CenterStart,
    bgColor: Color = MyAppTheme.colors.transparent,
    trailingContent: @Composable (() -> Unit)? = null
) {
    TopBar(
        isBackEnable = isBackEnable,
        onBackClick = onNavigationUp,
        content = {
            CustomText(
                text = title, style = titleStyle, color = MyAppTheme.colors.black
            )
        },
        modifier = modifier,
        contentAlignment = contentAlignment,
        bgColor = bgColor,
        trailingContent = trailingContent
    )
}