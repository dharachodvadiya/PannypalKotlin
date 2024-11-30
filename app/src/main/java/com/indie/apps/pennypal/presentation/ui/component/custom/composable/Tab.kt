package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun CustomTab(
    tabList: List<TabItemInfo>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    bgColor : Color = MyAppTheme.colors.itemBg,
    paddingValues: PaddingValues = PaddingValues(5.dp),
    tabHorizontalPadding : Dp = dimensionResource(R.dimen.button_horizontal_padding),
    tabVerticalPadding : Dp = dimensionResource(R.dimen.button_item_vertical_padding),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .roundedCornerBackground(bgColor)
            .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabList.forEachIndexed { index, tabItemInfo ->
            TabItem(
                tabItemInfo = tabItemInfo,
                onClick = { onTabSelected(index) },
                selected = index == selectedIndex,
                modifier = Modifier.weight(1f),
                horizontalPadding = tabHorizontalPadding,
                verticalPadding = tabVerticalPadding
            )
        }
    }
}

@Composable
private fun TabItem(
    tabItemInfo: TabItemInfo,
    onClick: () -> Unit,
    selected: Boolean = false,
    horizontalPadding : Dp = dimensionResource(R.dimen.button_horizontal_padding),
    verticalPadding : Dp = dimensionResource(R.dimen.button_item_vertical_padding),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val btnBgColor = if (selected) tabItemInfo.selectBgColor else tabItemInfo.unSelectBgColor
    val btnContentColor =
        if (selected) tabItemInfo.selectContentColor else tabItemInfo.unSelectContentColor
    PrimaryButton(
        bgColor = btnBgColor,
        onClick = onClick,
        horizontalPadding = horizontalPadding,
        verticalPadding = verticalPadding,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabItemInfo.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null, // Use null for no content description
                    tint = btnContentColor
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            CustomText(
                text = stringResource(id = tabItemInfo.title),
                style = MyAppTheme.typography.Medium45_29,
                color = btnContentColor
            )
        }
    }
}

@Preview
@Composable
private fun TabPreview() {
    PennyPalTheme(darkTheme = true) {
        //Tab()
    }
}