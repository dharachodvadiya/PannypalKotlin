package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme


@Composable
fun FlowRowItem(
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit,
    bgColor: Color = MyAppTheme.colors.transparent,
    textColor: Color? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val color =
        if (isSelected) MyAppTheme.colors.lightBlue1 else textColor ?: MyAppTheme.colors.gray2

    val shape = RoundedCornerShape(100.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .widthIn(0.dp, 110.dp)
            .padding(dimensionResource(id = R.dimen.item_padding))
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = color
                ),
                shape = shape
            )
            .background(color = bgColor, shape = shape)
            .clip(shape = shape)
            .clickableWithNoRipple { onClick() }
            .padding(
                horizontal = dimensionResource(R.dimen.bottom_bar_item_horizontal_padding),
                vertical = dimensionResource(R.dimen.bottom_bar_item_vertical_padding)
            )

    ) {
        CustomText(
            text = text,
            color = color,
            style = MyAppTheme.typography.Medium40,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }

}