package com.indie.apps.pannypal.presentation.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun MyAppTopBar(
    title: String,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.CenterStart,
    bgColor: Color = MyAppTheme.colors.white
) {
    TopBar(
        isBackEnable = true,
        onBackClick = onNavigationUp,
        content = {
            Text(
                text = title,
                style = MyAppTheme.typography.Semibold57,
                color = MyAppTheme.colors.black
            )
        },
        modifier = modifier,
        contentAlignment = contentAlignment,
        bgColor = bgColor
    )
}

@Composable
fun UserProfile(
    borderWidth : Float = 0f,
    modifier: Modifier = Modifier
) {
    val iconGradient = MyAppTheme.colors.gradientBlue
    val borderModifier = if (borderWidth > 0) {
        Modifier
            .border(
                BorderStroke(
                    width = borderWidth.dp,
                    color = MyAppTheme.colors.gray0
                ),
                shape = CircleShape
            )
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .then(borderModifier)
            .size(dimensionResource(id = R.dimen.user_image_bg_size))
            .shadow(
                color = MyAppTheme.colors.gray0,
                offsetX = (4).dp,
                offsetY = (5).dp,
                blurRadius = 5.dp,
            )
            .background(
                color = MyAppTheme.colors.white,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.user_image_size))
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(linearGradientsBrush(iconGradient), blendMode = BlendMode.SrcAtop)
                    }
                }
        )
    }
}

@Preview()
@Composable
private fun MyAppTopBarPreview() {
    PannyPalTheme {
        MyAppTopBar(
            title = "title",
            onNavigationUp = { },
            contentAlignment = Alignment.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserProfilePreview() {
    PannyPalTheme {
        UserProfile()
    }
}