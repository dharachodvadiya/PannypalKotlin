package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    onSeeAllTransactionClick: () -> Unit,
    onSeeAllMerchantClick: () -> Unit,
    onTransactionClick: (Long) -> Unit,
    bottomPadding: PaddingValues,
    addEditMerchantDataId: Long,
    onNavigationUp: () -> Unit,
    isAddMerchantDataSuccess: Boolean = false,
    isEditSuccess: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    /* val dataWithDayLazyPagingItems =
         overViewViewModel.pagedMerchantDataWithDay.collectAsLazyPagingItems()
     val merchantDataWithDayPagingState by overViewViewModel.merchantDataWithDayPagingState.collectAsStateWithLifecycle()
     merchantDataWithDayPagingState.update(dataWithDayLazyPagingItems)
     */

    val userData by overViewViewModel.userData.collectAsStateWithLifecycle()
    val currentMonthTotal by overViewViewModel.currentMonthTotal.collectAsStateWithLifecycle()
    val addDataAnimRun by overViewViewModel.addDataAnimRun.collectAsStateWithLifecycle()
    val editAnimRun by overViewViewModel.editAnimRun.collectAsStateWithLifecycle()

    val recentTransaction by overViewViewModel.recentTransaction.collectAsStateWithLifecycle()
    val recentMerchant by overViewViewModel.recentMerchant.collectAsStateWithLifecycle()
    val currentMonthCategory by overViewViewModel.monthlyCategory.collectAsStateWithLifecycle()

    if (userData != null) {
        Util.currentCurrencySymbol =
            overViewViewModel.getSymbolFromCurrencyCode(userData!!.currency)
    }

    var isAddDataSuccess by remember {
        mutableStateOf(false)
    }

    var addEditDataId by remember {
        mutableLongStateOf(-1L)
    }

    if (isAddDataSuccess != isAddMerchantDataSuccess) {
        if (isAddMerchantDataSuccess) {
            addEditDataId = addEditMerchantDataId
            overViewViewModel.addMerchantDataSuccess()
        }
        isAddDataSuccess = isAddMerchantDataSuccess
    }

    var isEditDataSuccess by remember {
        mutableStateOf(false)
    }

    if (isEditDataSuccess != isEditSuccess) {
        if (isEditSuccess) {
            addEditDataId = addEditMerchantDataId
            overViewViewModel.editDataSuccess()
        }
        isEditDataSuccess = isEditSuccess
    }

    Scaffold(
        /*topBar = {
            OverviewTopBar(
                onProfileClick = onProfileClick
            )
        }*//*, floatingActionButton = {
        OverviewAppFloatingButton(onClick = onNewEntry)
    }*/
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding)
                .padding(dimensionResource(id = R.dimen.padding)),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            if (currentMonthTotal == null) {
                LoadingWithProgress(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {

                OverviewTopBarProfile(onClick = {}, user = userData)

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

                OverviewData(
                    data = currentMonthTotal!!.toTotalWithCurrency(),
                    symbol = overViewViewModel.getSymbolFromCurrencyCode(currentMonthTotal!!.currency),
                    categoryList = currentMonthCategory,
                    recentTransaction = recentTransaction,
                    recentMerchant = recentMerchant,
                    onSeeAllTransactionClick = onSeeAllTransactionClick,
                    onSeeAllMerchantClick = onSeeAllMerchantClick,
                    merchantDataId = addEditDataId,
                    isAddMerchantDataSuccess = addDataAnimRun,
                    isEditMerchantDataSuccess = editAnimRun,
                    onTransactionClick = onTransactionClick,
                    onAnimStop = {
                        overViewViewModel.addMerchantDataSuccessAnimStop()
                    }
                )

                Spacer(modifier = Modifier.height(30.dp))
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
            onSeeAllTransactionClick = {},
            onSeeAllMerchantClick = {},
            onTransactionClick = {}
        )
    }
}