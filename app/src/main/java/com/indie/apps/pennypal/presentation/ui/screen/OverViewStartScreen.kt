package com.indie.apps.pennypal.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.screen.OverviewBalanceView
import com.indie.apps.pennypal.presentation.ui.component.screen.OverviewList
import com.indie.apps.pennypal.presentation.ui.component.screen.OverviewTopBar
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.presentation.viewmodel.OverViewViewModel
import com.indie.apps.pennypal.util.Resource

@Composable
fun OverViewStartScreen(
    overViewViewModel: OverViewViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    bottomPadding: PaddingValues,
    addEditMerchantDataId: Long,
    isAddMerchantDataSuccess: Boolean = false,
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
        is Resource.Loading -> {}
        is Resource.Success -> {
            amount = (uiState.data?.incomeAmount ?: 0.0) - (uiState.data?.expenseAmount ?: 0.0)
        }

        is Resource.Error -> {}
    }

    var isAddDataSuccess by remember {
        mutableStateOf(false)
    }

    var addDataId by remember {
        mutableStateOf(-1L)
    }

    if (isAddDataSuccess != isAddMerchantDataSuccess) {
        if (isAddMerchantDataSuccess) {
            addDataId = addEditMerchantDataId
            overViewViewModel.addMerchantDataSuccess()
        }
        isAddDataSuccess = isAddMerchantDataSuccess
    }

    /* LaunchedEffect(isAddMerchantDataSuccess) {

         if (isAddMerchantDataSuccess) {
             overViewViewModel.addMerchantDataSuccess()
         }

     }*/

    Scaffold(
        topBar = {
            OverviewTopBar(
                onProfileClick = onProfileClick
            )
        }/*, floatingActionButton = {
        OverviewAppFloatingButton(onClick = onNewEntry)
    }*/
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding)
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
                            merchantDataDailyTotalPagingState.isLoadMore,
                    bottomPadding = bottomPadding,
                    merchantDataId = addDataId,
                    isAddMerchantDataSuccess = overViewViewModel.addDataAnimRun.value,
                    onAnimStop = {
                        overViewViewModel.addMerchantDataSuccessAnimStop()
                    }
                )
            }

        }
    }
}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        OverViewStartScreen(
            onProfileClick = {},
            bottomPadding = PaddingValues(0.dp),
            addEditMerchantDataId = -1
        )
    }
}