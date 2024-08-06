package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val dataWithNameLazyPagingItems = overViewViewModel.pagedMerchantData.collectAsLazyPagingItems()
    val merchantDataWithNamePagingState by overViewViewModel.merchantDataWithNamePagingState.collectAsStateWithLifecycle()
    merchantDataWithNamePagingState.update(dataWithNameLazyPagingItems)

    val dailyTotalLazyPagingItems =
        overViewViewModel.pagedMerchantDataDailyTotal.collectAsLazyPagingItems()
    val merchantDataDailyTotalPagingState by
    overViewViewModel.merchantDataDailyTotalPagingState.collectAsStateWithLifecycle()
    merchantDataDailyTotalPagingState.update(dailyTotalLazyPagingItems)


    val uiState by overViewViewModel.uiState.collectAsStateWithLifecycle()

    var amount by remember {
        mutableDoubleStateOf(0.0)
    }

    when (uiState) {
        is Resource.Loading -> 0.0
        is Resource.Success -> {
            amount = (uiState.data?.incomeAmount ?: 0.0) - (uiState.data?.expenseAmount ?: 0.0)
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
            if (merchantDataWithNamePagingState.isRefresh ||
                merchantDataDailyTotalPagingState.isRefresh
            ) {
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
                    dataList = dataWithNameLazyPagingItems,
                    dailyTotalList = dailyTotalLazyPagingItems,
                    isLoadMore = merchantDataWithNamePagingState.isLoadMore ||
                            merchantDataDailyTotalPagingState.isLoadMore
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