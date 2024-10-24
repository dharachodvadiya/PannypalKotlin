package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    isClickable: Boolean = true,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth(),
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit),
    trailingContent: @Composable (() -> Unit)? = null,
    isSetDivider: Boolean = false,
    dividerWidth: Float = 2f,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    itemBgColor: Color = MyAppTheme.colors.itemBg
) {
    val bgColor = if (isSelected) MyAppTheme.colors.itemSelectedBg else itemBgColor
    val clickModifier =
        if (isClickable)
            Modifier
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick,
                )
        else Modifier
    Row(
        modifier = modifier
            .roundedCornerBackground(bgColor)
            .then(clickModifier)
            .padding(paddingValues)
            .padding(dimensionResource(id = R.dimen.item_inner_padding)),
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
                    .padding(start = 0.dp, bottom = 7.dp, top = 7.dp, end = 0.dp)
                    .drawBehind {
                        drawLine(
                            colorDivider,
                            Offset(0f, size.height + 20),
                            Offset(size.width, size.height + 20),
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
