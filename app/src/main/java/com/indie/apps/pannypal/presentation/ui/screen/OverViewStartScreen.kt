package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.presentation.ui.component.screen.OverviewAppFloatingButton
import com.indie.apps.pannypal.presentation.ui.component.screen.OverviewBalanceView
import com.indie.apps.pannypal.presentation.ui.component.screen.OverviewList
import com.indie.apps.pannypal.presentation.ui.component.screen.OverviewTopBar
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun OverViewStartScreen(
    onProfileClick: () -> Unit,
    onNewEntry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            OverviewTopBar(
                onSearchTextChange = {},
                onProfileClick = onProfileClick
            )
        },
        floatingActionButton = {
            OverviewAppFloatingButton(onClick = onNewEntry)
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            OverviewBalanceView(-10000.0)
            OverviewList()
        }
    }
}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PannyPalTheme {
        OverViewStartScreen({}, {})
    }
}