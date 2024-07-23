package com.indie.apps.pannypal.presentation.ui.component.custom.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme

@Composable
fun ListItem(
    isClickable: Boolean = true,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(),
    leadingIcon: @Composable() (() -> Unit)? = null,
    content: @Composable() (() -> Unit),
    trailingContent: @Composable() (() -> Unit)? = null,
    isSetDivider: Boolean = false,
    dividerWidth: Float = 2f
) {
    val bgColor = if (isSelected) MyAppTheme.colors.brandBg else MyAppTheme.colors.white

    Surface(
        onClick = onClick,
        enabled = isClickable,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .background(bgColor)
                .padding(vertical = 1.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_content_padding)))
            }
            if (isSetDivider) {
                val colorDivider = MyAppTheme.colors.gray1
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 0.dp, bottom = 15.dp, top = 15.dp, end = 0.dp)
                        .drawBehind {
                            drawLine(
                                colorDivider,
                                Offset(0f, size.height + 35),
                                Offset(size.width, size.height + 35),
                                dividerWidth
                            )
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        content()
                    }
                    if (trailingContent != null) {
                        trailingContent()
                    }
                }
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
                if (trailingContent != null) {
                    trailingContent()
                }
            }

        }
    }
}
