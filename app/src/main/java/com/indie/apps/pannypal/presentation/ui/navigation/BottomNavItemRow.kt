package com.indie.apps.pannypal.presentation.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.LinearGradientsColor
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme


@Composable
fun BottomNavigationBarCustom(
    tabs: Array<BottomNavItem>,
    onTabSelected: (BottomNavItem) -> Unit,
    currentTab: BottomNavItem,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(Util.BottomNavHeight)
            .fillMaxWidth()
    ){
        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    brush = LinearGradientsColor(MyAppTheme.colors.button_gradient_blue)
                )
        ) {
            tabs.forEach { item ->

                BottomNavigationBarCustomItem(item, onTabSelected, currentTab)
            }
        }
    }
}

@Composable
fun BottomNavigationBarCustomItem(
    item: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit,
    currentTab: BottomNavItem,
    modifier: Modifier = Modifier
){

    val isSelected = currentTab == item
    if (isSelected){
        modifier
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    MyAppTheme.colors.white
                ),
                shape = RoundedCornerShape(100.dp)
            )
            .padding(
                horizontal = Util.BottomNavButtonHorizontalPadding,
                vertical = Util.BottomNavButtonVerticalPadding)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                onTabSelected(item)
            }
    ) {
        Icon(
            imageVector = if(isSelected) item.selectedIcon else item.unSelectedIcon,
            contentDescription = stringResource(item.title),
            tint = if(isSelected) MyAppTheme.colors.white else MyAppTheme.colors.inActive_light
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(item.title),
                color = MyAppTheme.colors.white,
                style = MyAppTheme.typography.Medium45_29)
        }

    }
}

@Composable
fun BottomNavigationBarCustom1(
    tabs: Array<BottomNavItem>,
    onTabSelected: (BottomNavItem) -> Unit,
    currentTab: BottomNavItem,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(Util.BottomNavHeight)
            .fillMaxWidth()
    ){
        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { item ->

                BottomNavigationBarCustom1Item(item, onTabSelected, currentTab)
            }
        }
    }
}

@Composable
fun BottomNavigationBarCustom1Item(
    item: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit,
    currentTab: BottomNavItem,
    modifier: Modifier = Modifier
){
    val isSelected = currentTab == item

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                onTabSelected(item)
            }
    ) {
        Icon(
            imageVector = if(isSelected) item.selectedIcon else item.unSelectedIcon,
            contentDescription = stringResource(item.title),
            tint = if(isSelected) MyAppTheme.colors.brand else MyAppTheme.colors.inActive_dark
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(item.title),
                color = MyAppTheme.colors.brand,
                style = MyAppTheme.typography.Medium45_29)
        }

    }
}
/*
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun BottomNavPreviewDarkModeCustom() {
    PannyPalTheme {
        BottomNavigationBarCustom(
            tabs = BottomNavItem.values(),
            onTabSelected = {},
            currentTab = BottomNavItem.OVERVIEW
        )
    }
}*/

@Preview
@Composable
private fun BottomNavPreviewCustom() {
    PannyPalTheme {
        BottomNavigationBarCustom(
            tabs = BottomNavItem.values(),
            onTabSelected = {},
            currentTab = BottomNavItem.OVERVIEW
        )
    }
}
/*
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun BottomNavPreviewDarkModeCustom1() {
    PannyPalTheme {
        BottomNavigationBarCustom1(
            tabs = BottomNavItem.values(),
            onTabSelected = {},
            currentTab = BottomNavItem.OVERVIEW
        )
    }
}*/

@Preview
@Composable
private fun BottomNavPreviewCustom1() {
    PannyPalTheme {
        BottomNavigationBarCustom1(
            tabs = BottomNavItem.values(),
            onTabSelected = {},
            currentTab = BottomNavItem.OVERVIEW
        )
    }
}