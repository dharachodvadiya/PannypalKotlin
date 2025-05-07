package com.indie.apps.pennypal.presentation.ui.component.composable.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme


@Composable
fun NoDataMessage(
    title: String,
    details: String,
    painterRes: Int = R.drawable.receipt_long_off,
    iconSize: Dp = 50.dp,
    titleTextStyle: TextStyle = MyAppTheme.typography.Regular51,
    detailsTextStyle: TextStyle = MyAppTheme.typography.Regular44,
    titleColor: Color = MyAppTheme.colors.gray2,
    detailsColor: Color = MyAppTheme.colors.gray3,
    iconColor: Color = MyAppTheme.colors.gray2,
    isClickable: Boolean = false,
    onClick: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxSize()
) {
    Box(
        modifier = modifier
            .background(MyAppTheme.colors.transparent)
            .clickableWithNoRipple(enabled = isClickable) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.75f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_inner_padding))
        ) {

            Icon(
                painter = painterResource(painterRes),
                contentDescription = "no transaction",
                tint = iconColor,
                modifier = Modifier.size(iconSize)
            )

            CustomText(
                text = title,
                style = titleTextStyle,
                color = titleColor,
                textAlign = TextAlign.Center
            )
            CustomText(
                text = details,
                style = detailsTextStyle,
                color = detailsColor,
                textAlign = TextAlign.Center
            )

        }
    }
}