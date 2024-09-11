package com.indie.apps.pennypal.presentation.ui.component.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.linearGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme


@Composable
fun BottomNavigationBarCustom(
    tabs: Array<BottomNavItem>,
    onTabSelected: (BottomNavItem) -> Unit,
    currentTab: BottomNavItem,
    modifier: Modifier = Modifier,
    bottomBarState: Boolean = true
) {
    if (bottomBarState) {
        Surface(
            modifier = modifier
                .height(dimensionResource(R.dimen.bottom_bar))
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Absolute.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue)
                    )
            ) {

                tabs.forEach { item ->
                    BottomNavigationBarCustomItem(item, onTabSelected, currentTab)
                }
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
) {
    Surface(
        onClick = {
            onTabSelected(item)
        },
        modifier = modifier
            .semantics { role = Role.Button },
        shape = RoundedCornerShape(100.dp),
        color = MyAppTheme.colors.transparent
    ) {
        val isSelected = currentTab == item
        val borderModifier = if (isSelected) {
            Modifier
                .border(
                    BorderStroke(
                        width = 1.dp,
                        color = MyAppTheme.colors.white
                    ),
                    shape = RoundedCornerShape(100.dp)
                )
                .padding(
                    horizontal = dimensionResource(R.dimen.bottom_bar_item_horizontal_padding),
                    vertical = dimensionResource(R.dimen.bottom_bar_item_vertical_padding)
                )
        } else {
            Modifier
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = borderModifier

        ) {
            Icon(
                painter = if (isSelected)
                    painterResource(id = item.selectedIcon)
                else painterResource(
                    id = item.unSelectedIcon
                ),
                contentDescription = stringResource(item.title),
                tint = if (isSelected) MyAppTheme.colors.white else MyAppTheme.colors.inactiveLight
            )
            if (isSelected) {
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(item.title),
                    color = MyAppTheme.colors.white,
                    style = MyAppTheme.typography.Medium45_29
                )
            }

        }
    }
}

@Composable
fun BottomNavigationBarCustom1(
    tabs: Array<BottomNavItem>,
    onTabSelected: (BottomNavItem) -> Unit,
    currentTab: BottomNavItem,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBarState: Boolean = true
) {
    if (bottomBarState) {
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current

        val offsetStep = (with(density) { configuration.screenWidthDp.dp.roundToPx() }) / (3 * 2)
        val offset = offsetStep + 2 * offsetStep
        val circleRadius = 26.dp
        val barShape = BarShape(
            offset = offset.toFloat(),
            circleRadius = circleRadius,
            cornerRadius = 0.dp,
        )

        Box(
            modifier = modifier
                .height(dimensionResource(R.dimen.bottom_bar) + circleRadius)
                .fillMaxWidth()
                .background(MyAppTheme.colors.transparent)
                .clickable(onClick = { }, enabled = false, role = Role.Button),
            contentAlignment = Alignment.BottomCenter
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    //.offset { IntOffset(0, -90) }
                    .padding(PaddingValues(bottom = 30.dp))
                    .zIndex(1f)
                    .size(circleRadius * 2)
                    .clip(CircleShape)
                    .background(MyAppTheme.colors.lightBlue2)
                    .clickable(onClick = onAddClick, enabled = true, role = Role.Button)
            ) {
                Icon(Icons.Default.Add, "Add", tint = MyAppTheme.colors.black)
            }

            Row(
                horizontalArrangement = Arrangement.Absolute.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .graphicsLayer {
                        shape = barShape
                        clip = true
                    }
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.bottom_bar))
                    .background(MyAppTheme.colors.bottomBg)
            ) {
                tabs.forEachIndexed() {index,  item ->
                    BottomNavigationBarCustom1Item(
                        item = item,
                        onTabSelected = { onTabSelected(item) },
                        currentTab = currentTab,
                    )

                    if(tabs.size %2 ==0 && (tabs.size/2) == index+1)
                    {
                        Box(modifier = Modifier.size(24.dp))
                    }
                }
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
) {
    /*Surface(
        onClick = {
            onTabSelected(item)
        },
        modifier = modifier
            .semantics { role = Role.Button },
        //shape = RoundedCornerShape(100.dp),
        //contentColor = MyAppTheme.colors.transparent
    ) {
        val isSelected = currentTab == item

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(MyAppTheme.colors.bottomBg)
        ) {
            Icon(
                painter = if (isSelected)
                    painterResource(id = item.selectedIcon)
                else painterResource(
                    id = item.unSelectedIcon
                ),
                contentDescription = stringResource(item.title),
                tint = if (isSelected) MyAppTheme.colors.lightBlue1 else MyAppTheme.colors.inactiveDark
            )
            *//* if (isSelected) {
                 Spacer(modifier = Modifier.width(5.dp))
                 Text(
                     text = stringResource(item.title),
                     color = MyAppTheme.colors.lightBlue1,
                     style = MyAppTheme.typography.Medium45_29
                 )
             }*//*

        }
    }*/

    val isSelected = currentTab == item

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .roundedCornerBackground(MyAppTheme.colors.transparent)
            //.background(MyAppTheme.colors.bottomBg)
            .clickable {  onTabSelected(item) }
    ) {
        Icon(
            painter = if (isSelected)
                painterResource(id = item.selectedIcon)
            else painterResource(
                id = item.unSelectedIcon
            ),
            contentDescription = stringResource(item.title),
            tint = if (isSelected) MyAppTheme.colors.lightBlue1 else MyAppTheme.colors.inactiveDark
        )
        /* if (isSelected) {
             Spacer(modifier = Modifier.width(5.dp))
             Text(
                 text = stringResource(item.title),
                 color = MyAppTheme.colors.lightBlue1,
                 style = MyAppTheme.typography.Medium45_29
             )
         }*/

    }

}
/*
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun BottomNavPreviewDarkModeCustom() {
    PennyPalTheme {
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
    PennyPalTheme {
        BottomNavigationBarCustom(
            tabs = BottomNavItem.entries.toTypedArray(),
            onTabSelected = {},
            currentTab = BottomNavItem.OVERVIEW
        )
    }
}
/*
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun BottomNavPreviewDarkModeCustom1() {
    PennyPalTheme {
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
    PennyPalTheme(darkTheme = true) {
        BottomNavigationBarCustom1(
            tabs = BottomNavItem.entries.toTypedArray(),
            onTabSelected = {},
            currentTab = BottomNavItem.OVERVIEW,
            onAddClick = {}
        )
    }
}