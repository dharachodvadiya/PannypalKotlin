package com.indie.apps.pannypal.presentation.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme


@Composable
fun BottomNavigationBar(
    tabs: Array<BottomNavItem>,
    onTabSelected: (BottomNavItem) -> Unit,
    currentTab: BottomNavItem
) {
    Surface(
        modifier = Modifier
            .height(BottomNavHeight)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary
    ){
        NavigationBar {
            tabs.forEach { item ->

                val isSelected = currentTab == item
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        onTabSelected(item)
                    },
                    icon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(item.title)
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = stringResource(item.title))
                            }

                        }

                    }

                )
            }
        }
    }
}

private val BottomNavHeight = 56.dp
private const val InactiveTabOpacity = 0.60f

@Preview
@Composable
private fun BottomNavPreview() {
    PannyPalTheme {
        BottomNavigationBar(
            tabs = BottomNavItem.values(),
            onTabSelected = {},
            currentTab = BottomNavItem.OVERVIEW
        )
    }
}