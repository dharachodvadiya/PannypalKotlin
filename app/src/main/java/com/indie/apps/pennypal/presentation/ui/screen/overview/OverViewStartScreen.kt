package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.DoubleBackExitApp
import com.indie.apps.pennypal.presentation.ui.screen.InAppFeedbackViewModel
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun OverViewStartScreen(
    overViewViewModel: OverViewViewModel = hiltViewModel(),
    inAppFeedbackViewModel: InAppFeedbackViewModel = hiltViewModel(),
    onMerchantClick: (Long) -> Unit,
    onSeeAllTransactionClick: () -> Unit,
    onSeeAllMerchantClick: () -> Unit,
    onExploreAnalysisClick: () -> Unit,
    onExploreBudgetClick: () -> Unit,
    onSetBudgetClick: (Int) -> Unit,
    onTransactionClick: (Long) -> Unit,
    addEditMerchantDataId: Long,
    addMerchantId: Long,
    onAddMerchant: () -> Unit,
    isAddMerchantDataSuccess: Boolean = false,
    isEditSuccess: Boolean = false,
    bottomPadding: PaddingValues,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {


    val localContext = LocalContext.current

    val currentPeriod by overViewViewModel.currentPeriod.collectAsStateWithLifecycle()
    // val currency by overViewViewModel.currency.collectAsStateWithLifecycle()
    val isSubscribed by overViewViewModel.isSubscribed.collectAsStateWithLifecycle()
    val userData by overViewViewModel.userData.collectAsStateWithLifecycle()
    val currentTotal by overViewViewModel.currentTotal.collectAsStateWithLifecycle()
    val addDataAnimRun by overViewViewModel.addDataAnimRun.collectAsStateWithLifecycle()
    val editAnimRun by overViewViewModel.editAnimRun.collectAsStateWithLifecycle()
    val addMerchantAnimRun by overViewViewModel.addMerchantAnimRun.collectAsStateWithLifecycle()

    val recentTransaction by overViewViewModel.recentTransaction.collectAsStateWithLifecycle()
    val recentMerchant by overViewViewModel.recentMerchant.collectAsStateWithLifecycle()
    val currentMonthCategory by overViewViewModel.monthlyCategoryExpense.collectAsStateWithLifecycle()
    val budgetState by overViewViewModel.budgetState.collectAsStateWithLifecycle()
    var currentBudgetPeriod by remember {
        mutableStateOf(PeriodType.MONTH)
    }
    /*
        if (userData != null) {
            Util.currentCurrencySymbol =
                overViewViewModel.getSymbolFromCountryCode(userData!!.currency)
        }*/
    LaunchedEffect(addMerchantId) {
        if (addMerchantId != -1L) {
            overViewViewModel.addMerchantSuccess()

            inAppFeedbackViewModel.triggerReview(localContext)
        }
    }

    var addEditDataId by remember {
        mutableLongStateOf(-1L)
    }
    LaunchedEffect(addEditMerchantDataId) {
        if (isAddMerchantDataSuccess) {
            addEditDataId = addEditMerchantDataId
            overViewViewModel.addMerchantDataSuccess()

            inAppFeedbackViewModel.triggerReview(localContext)
        }
        if (isEditSuccess) {
            addEditDataId = addEditMerchantDataId
            overViewViewModel.editDataSuccess()
        }
    }

    DoubleBackExitApp()

    Scaffold { topBarPadding ->

        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(dimensionResource(id = R.dimen.padding))
                .padding(
                    top = topBarPadding.calculateTopPadding(),
                    bottom = bottomPadding.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            /* if (currentMonthTotal == null) {
                 LoadingWithProgress(
                     modifier = Modifier
                         .fillMaxWidth()
                         .weight(1f)
                 )
             } else {*/

            OverviewTopBarProfile(
                onClick = {},
                user = userData,
                isSubscribed = isSubscribed,
                onSubscriptionChanged = overViewViewModel::onSubscriptionChanged
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

            OverviewData(
                currentPeriod = currentPeriod,
                data = currentTotal,
                //currency = currency,
                categoryList = currentMonthCategory,
                recentTransaction = recentTransaction,
                recentMerchant = recentMerchant,
                onSeeAllTransactionClick = onSeeAllTransactionClick,
                onSeeAllMerchantClick = onSeeAllMerchantClick,
                onExploreAnalysisClick = onExploreAnalysisClick,
                onExploreBudgetClick = onExploreBudgetClick,
                merchantDataId = addEditDataId,
                isAddMerchantDataSuccess = addDataAnimRun,
                isEditMerchantDataSuccess = editAnimRun,
                onTransactionClick = onTransactionClick,
                onAnimStop = {
                    overViewViewModel.addMerchantDataSuccessAnimStop()
                },
                budgetWithSpentAndCategoryIdList = budgetState.firstOrNull { it.periodType == currentBudgetPeriod.id },
                selectBudgetPeriod = currentBudgetPeriod,
                onSelectBudgetPeriod = {
                    currentBudgetPeriod = if (currentBudgetPeriod == PeriodType.MONTH)
                        PeriodType.YEAR
                    else
                        PeriodType.MONTH
                },
                onAddMerchant = onAddMerchant,
                onMerchantAnimStop = {
                    overViewViewModel.addMerchantSuccessAnimStop()
                },
                isAddMerchantSuccess = addMerchantAnimRun,
                merchantId = addMerchantId,
                onSetBudgetClick = onSetBudgetClick,
                onMerchantClick = onMerchantClick,
                isSelectionEnable = budgetState.count { it.periodType == PeriodType.MONTH.id || it.periodType == PeriodType.YEAR.id } == 2
            )

            Spacer(modifier = Modifier.height(30.dp))
            //}
        }
    }

}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        OverViewStartScreen(
            addEditMerchantDataId = -1,
            onSeeAllTransactionClick = {},
            onSeeAllMerchantClick = {},
            onTransactionClick = {},
            onExploreAnalysisClick = {},
            onExploreBudgetClick = {},
            onAddMerchant = {},
            addMerchantId = -1L,
            onSetBudgetClick = {},
            bottomPadding = PaddingValues(0.dp),
            onMerchantClick = {}
        )
    }
}