package com.indie.apps.pannypal.presentation.ui.component.custom.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun RoundImage(
    imageVector: ImageVector,
    tint: Color,
    backGround: Color,
    contentDescription: String,
    modifier: Modifier = Modifier.size(dimensionResource(id = R.dimen.item_image))
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = backGround,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Preview()
@Composable
private fun RoundImagePreview() {
    PannyPalTheme {
        RoundImage(
            imageVector = Icons.Filled.Person,
            tint = MyAppTheme.colors.white,
            backGround = MyAppTheme.colors.brandBg,
            contentDescription = "person"
        )
    }
}