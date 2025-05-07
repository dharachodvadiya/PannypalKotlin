package com.indie.apps.pennypal.presentation.ui.component.composable.common

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.component.extension.shadow
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun UserProfileRound(
    borderWidth: Float = 0f, @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    MyAppTheme.colors.gradientBlue
    val borderModifier = if (borderWidth > 0) {
        Modifier.border(
            BorderStroke(
                width = borderWidth.dp, color = MyAppTheme.colors.gray2
            ), shape = CircleShape
        )
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .then(borderModifier)
            .size(dimensionResource(id = R.dimen.user_image_bg_size))
            .shadow(
                color = MyAppTheme.colors.brandBg,
                offsetX = (4).dp,
                offsetY = (5).dp,
                blurRadius = 5.dp,
            )
            .background(
                color = MyAppTheme.colors.lightBlue2, shape = CircleShape
            ), contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User",
            modifier = Modifier.size(dimensionResource(id = R.dimen.user_image_size))/*.graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(linearGradientsBrush(iconGradient), blendMode = BlendMode.SrcAtop)
                }
            }*/,
            tint = MyAppTheme.colors.black
        )
    }
}

@Composable
fun UserProfileRect(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Center,
        modifier = modifier
            .roundedCornerBackground(
                MyAppTheme.colors.white, BorderStroke(
                    width = 1.dp,
                    color = MyAppTheme.colors.gray1
                )
            )
            .padding(dimensionResource(R.dimen.button_horizontal_padding))
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Profile",
            tint = MyAppTheme.colors.gray1
        )
    }
}