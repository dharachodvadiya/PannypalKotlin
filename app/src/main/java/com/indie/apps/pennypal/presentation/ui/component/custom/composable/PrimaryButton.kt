package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    bgBrush: Brush? = null,
    bgColor: Color = MyAppTheme.colors.brand,
    borderStroke: BorderStroke? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val containerColor = if (enabled) bgColor else MyAppTheme.colors.gray2
    val contentColor =
        if (enabled) MyAppTheme.colors.white else MyAppTheme.colors.white.copy(alpha = 0.1f)
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .background(MyAppTheme.colors.transparent)
            .semantics { role = Role.Button },
        shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
        contentColor = contentColor,
        border = borderStroke
    ) {

        val buttonModifier = if (bgBrush != null && enabled) {
            Modifier.background(brush = bgBrush)
        } else {
            Modifier.background(color = containerColor)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Center,
            modifier = buttonModifier
                .padding(
                    horizontal = dimensionResource(R.dimen.button_horizontal_padding),
                    vertical = dimensionResource(R.dimen.button_item_vertical_padding)
                ),
            content = content
        )
    }

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
                Text(
                    text = "test"
                )
            }
        }
    }
}