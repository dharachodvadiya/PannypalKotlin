package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.toTotalWithCurrency
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun OverViewStartScreen(
    overViewViewModel: OverViewViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onSeeAllClick: () -> Unit,
    bottomPadding: PaddingValues,
    addEditMerchantDataId: Long,
    onNavigationUp: () -> Unit,
    isAddMerchantDataSuccess: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
   /* val dataWithDayLazyPagingItems =
        overViewViewModel.pagedMerchantDataWithDay.collectAsLazyPagingItems()
    val merchantDataWithDayPagingState by overViewViewModel.merchantDataWithDayPagingState.collectAsStateWithLifecycle()
    merchantDataWithDayPagingState.update(dataWithDayLazyPagingItems)
    */
    //val userData by overViewViewModel.userData.collectAsStateWithLifecycle()
    val monthlyTotal by overViewViewModel.monthlyTotal.collectAsStateWithLifecycle()
    val addDataAnimRun by overViewViewModel.addDataAnimRun.collectAsStateWithLifecycle()

    val recentTransaction by overViewViewModel.recentTransaction.collectAsStateWithLifecycle()

    /*var amount by remember {
        mutableDoubleStateOf(0.0)
    }*/

    if (monthlyTotal.isNotEmpty()) {
        Util.currentCurrencySymbol =
            overViewViewModel.getSymbolFromCurrencyCode(monthlyTotal[0].currency)
        //amount = (monthlyTotal[0].totalIncome) - (monthlyTotal[0].totalExpense)

    }

    var isAddDataSuccess by remember {
        mutableStateOf(false)
    }

    var addDataId by remember {
        mutableLongStateOf(-1L)
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
                .padding(dimensionResource(id = R.dimen.padding))
        ) {
            if (monthlyTotal.isEmpty()) {
                LoadingWithProgress(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                OverviewBalanceView(
                    data = monthlyTotal[0].toTotalWithCurrency(),
                    symbol = overViewViewModel.getSymbolFromCurrencyCode(monthlyTotal[0].currency),
                )
                /* OverviewList(
                     dataWithDayList = dataWithDayLazyPagingItems,
                     isLoadMore = merchantDataWithDayPagingState.isLoadMore,
                     bottomPadding = bottomPadding,
                     merchantDataId = addDataId,
                     isAddMerchantDataSuccess = addDataAnimRun,
                     onAnimStop = {
                         overViewViewModel.addMerchantDataSuccessAnimStop()
                     }
                 )*/
                Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.overview_item_padding)))
                OverviewData(
                    recentTransaction = recentTransaction,
                    onSeeAllTransactionClick = onSeeAllClick
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
            addEditMerchantDataId = -1,
            onNavigationUp = {},
            onSeeAllClick = {}
        )
    }
}