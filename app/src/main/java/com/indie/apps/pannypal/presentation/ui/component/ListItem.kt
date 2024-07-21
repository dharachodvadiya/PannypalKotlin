package com.indie.apps.pannypal.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun ListItem(
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth(),
    leadingIcon: @Composable() (() -> Unit)? = null,
    content: @Composable() (() -> Unit),
    trailingContent: @Composable() (() -> Unit),
    isSetDivider: Boolean = false,
    dividerWidth: Float = 2f
) {
    val bgColor = if (isSelected) MyAppTheme.colors.brandBg else MyAppTheme.colors.white

    Surface(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 1.dp)
                .background(bgColor),
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
                    trailingContent()
                }
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    content()
                }
                trailingContent()
            }

        }
    }
}

@Composable
fun OverviewListItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isDateShow: Boolean = false
) {
    Column(modifier = modifier.background(MyAppTheme.colors.white)) {
        if (isDateShow) {
            // TODO: change total color based on amount sum
            val totalTextColor = MyAppTheme.colors.redBg

            Text(
                text = "Day",
                style = MyAppTheme.typography.Semibold45,
                color = MyAppTheme.colors.gray2
            )
            Text(
                text = Util.getFormattedStringWithSymbole(0.0),
                style = MyAppTheme.typography.Semibold52_5,
                color = totalTextColor,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }

        // TODO: change Round Image Icon and bg color based on amount
        val imageVector = Icons.Default.SouthWest
        val bgColor = MyAppTheme.colors.redBg

        ListItem(
            onClick = onClick,
            leadingIcon = {
                RoundImage(
                    imageVector = imageVector,
                    tint = MyAppTheme.colors.white,
                    backGround = bgColor,
                    contentDescription = "person"
                )
            },
            content = {
                Column {
                    Text(
                        text = "Name",
                        style = MyAppTheme.typography.Bold52_5,
                        color = MyAppTheme.colors.black
                    )
                    Text(
                        text = "Description",
                        style = MyAppTheme.typography.Medium33,
                        color = MyAppTheme.colors.gray2
                    )
                }
            },
            trailingContent = {
                Text(
                    text = Util.getFormattedStringWithSymbole(0.0),
                    style = MyAppTheme.typography.Bold52_5,
                    color = MyAppTheme.colors.black
                )
            },
            isSetDivider = true
        )
    }

}

@Preview
@Composable
private fun ItemPreview() {
    PannyPalTheme {
        OverviewListItem({})
    }
}