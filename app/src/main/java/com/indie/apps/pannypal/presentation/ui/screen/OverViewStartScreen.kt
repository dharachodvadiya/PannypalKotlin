package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.indie.apps.pannypal.presentation.ui.component.screen.OverviewAppFloatingButton
import com.indie.apps.pannypal.presentation.ui.component.screen.OverviewBalanceView
import com.indie.apps.pannypal.presentation.ui.component.screen.OverviewList
import com.indie.apps.pannypal.presentation.ui.component.screen.OverviewTopBar
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.OverViewViewModel
import com.indie.apps.pannypal.util.Resource

@Composable
fun OverViewStartScreen(
    overViewViewModel: OverViewViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onNewEntry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyPagingData = overViewViewModel.pagedData.collectAsLazyPagingItems()
    overViewViewModel.pagingState.update(lazyPagingData)

    val uiState by overViewViewModel.uiState.collectAsStateWithLifecycle()

    val amount = when (uiState) {
        is Resource.Loading -> 0.0
        is Resource.Success -> {
            (uiState.data?.incomeAmount ?: 0.0) - (uiState.data?.expenseAmount ?: 0.0)
        }
        is Resource.Error -> 0.0
    }

    Scaffold(topBar = {
        OverviewTopBar(
            onProfileClick = onProfileClick
        )
    }, floatingActionButton = {
        OverviewAppFloatingButton(onClick = onNewEntry)
    }) { innerPadding ->

        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            if (overViewViewModel.pagingState.isRefresh) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    CircularProgressIndicator()
                }
            } else {
                OverviewBalanceView(amount)
                OverviewList(
                    dataList = lazyPagingData, isLoadMore = overViewViewModel.pagingState.isLoadMore
                )
            }

        }
    }
}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PannyPalTheme {
        OverViewStartScreen(onProfileClick = {}, onNewEntry = {})
    }
}