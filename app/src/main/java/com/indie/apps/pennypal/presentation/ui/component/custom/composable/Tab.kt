package com.indie.apps.pennypal.presentation.ui.component.custom.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun CustomTab(
    tabList: List<TabItemInfo>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            .padding(5.dp),
    ) {
        tabList.forEachIndexed { index, tabItemInfo ->
            TabItem(
                tabItemInfo = tabItemInfo,
                onClick = { onTabSelected(index) },
                selected = index == selectedIndex,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TabItem(
    tabItemInfo: TabItemInfo,
    onClick: () -> Unit,
    selected: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val btnBgColor = if (selected) tabItemInfo.selectBgColor else tabItemInfo.unSelectBgColor
    val btnContentColor =
        if (selected) tabItemInfo.selectContentColor else tabItemInfo.unSelectContentColor
    PrimaryButton(
        bgColor = btnBgColor,
        onClick = onClick,
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