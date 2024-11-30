package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    bgBrush: Brush? = null,
    bgColor: Color = MyAppTheme.colors.brand,
    borderStroke: BorderStroke? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalPadding : Dp = dimensionResource(R.dimen.button_horizontal_padding),
    verticalPadding : Dp = dimensionResource(R.dimen.button_item_vertical_padding),
    content: @Composable RowScope.() -> Unit,
) {
    val containerColor = if (enabled) bgColor else MyAppTheme.colors.gray2

    val buttonModifier = if (bgBrush != null && enabled) {
        Modifier.background(brush = bgBrush)
    } else {
        Modifier.background(color = containerColor)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Center,
        modifier = modifier
            .roundedCornerBackground(MyAppTheme.colors.transparent, borderStroke)
            .then(buttonModifier)
            .clickable(enabled = enabled) { onClick() }
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        content = content
    )

}


@Preview
@Composable
private fun PrimaryButtonPreview() {
    PennyPalTheme(darkTheme = true) {
        PrimaryButton(
            enabled = false,
            onClick = {}
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "test"
                )
                Spacer(modifier = Modifier.width(5.dp))
                CustomText(
                    text = "test"
                )
            }
        }
    }
}