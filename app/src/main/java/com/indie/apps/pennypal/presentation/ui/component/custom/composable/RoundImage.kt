package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun RoundImage(
    imageVector: ImageVector,
    imageVectorSize: Dp = dimensionResource(id = R.dimen.default_icon_size),
    tint: Color = MyAppTheme.colors.black,
    brush: Brush? = null,
    backGround: Color,
    innerPadding: Dp = 0.dp,
    contentDescription: String,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.size(dimensionResource(id = R.dimen.item_image))
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = backGround,
                shape = CircleShape
            ).padding(innerPadding)
    ) {
        if (brush != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(imageVectorSize)
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush, blendMode = BlendMode.SrcAtop)
                        }
                    }
            )
        } else {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = tint
            )
        }

    }
}

@Preview
@Composable
private fun RoundImagePreview() {
    PennyPalTheme(darkTheme = true) {
        RoundImage(
            imageVector = Icons.Filled.Person,
            tint = MyAppTheme.colors.white,
            backGround = MyAppTheme.colors.brandBg,
            contentDescription = "person"
        )
    }
}