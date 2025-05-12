package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.DoubleBackExitApp
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.ads.AdViewModel
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.feedback.InAppFeedbackViewModel
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.app_enum.PeriodType

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun OverViewStartScreen(
    overViewViewModel: OverViewViewModel = hiltViewModel(),
    inAppFeedbackViewModel: InAppFeedbackViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel(),
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
    isDeleteSuccess: Boolean = false,
    isEditSuccess: Boolean = false,
    bottomPadding: PaddingValues,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {


    val localContext = LocalContext.current

    // Load ad when screen is created
    LaunchedEffect(Unit) {
        adViewModel.loadInterstitialAd()
    }

    val currentPeriod by overViewViewModel.currentPeriod.collectAsStateWithLifecycle()
    // val currency by overViewViewModel.currency.collectAsStateWithLifecycle()
    val userData by overViewViewModel.userData.collectAsStateWithLifecycle()
    val currentTotal by overViewViewModel.currentTotal.collectAsStateWithLifecycle()
    val currentMerchantAnim by overViewViewModel.currentMerchantAnim.collectAsStateWithLifecycle()
    val currentMerchantAnimId by overViewViewModel.currentMerchantAnimId.collectAsStateWithLifecycle()
    val currentMerchantDataAnim by overViewViewModel.currentMerchantDataAnim.collectAsStateWithLifecycle()
    val currentMerchantDataAnimId by overViewViewModel.currentMerchantDataAnimId.collectAsStateWithLifecycle()

    val recentTransaction by overViewViewModel.recentTransaction.collectAsStateWithLifecycle()
    val recentMerchant by overViewViewModel.recentMerchant.collectAsStateWithLifecycle()
    val currentMonthCategory by overViewViewModel.monthlyCategoryExpense.collectAsStateWithLifecycle()
    val budgetState by overViewViewModel.budgetState.collectAsStateWithLifecycle()
    var currentBudgetPeriod by remember {
        mutableStateOf(PeriodType.MONTH)
    }

    val context = LocalContext.current
    val merchantDeleteToast = stringResource(id = R.string.data_delete_success_message)

    LaunchedEffect(addMerchantId) {
        if (addMerchantId != -1L) {
            overViewViewModel.addMerchantSuccess(addMerchantId)

            inAppFeedbackViewModel.triggerReview(localContext)
        }
    }
    LaunchedEffect(addEditMerchantDataId) {
        if (isAddMerchantDataSuccess) {
            //addEditDataId = addEditMerchantDataId
            overViewViewModel.addMerchantDataSuccess(addEditMerchantDataId)

            inAppFeedbackViewModel.triggerReview(localContext)
        } else if (isEditSuccess) {
            //addEditDataId = addEditMerchantDataId
            overViewViewModel.editDataSuccess(addEditMerchantDataId)
        } else if (isDeleteSuccess) {
            overViewViewModel.onDeleteTransactionFromEditScreenClick(addEditMerchantDataId) {
                context.showToast(merchantDeleteToast)
            }
        }

    }

    DoubleBackExitApp()

    Scaffold { topBarPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                //.padding(dimensionResource(id = R.dimen.padding))
                .padding(
                    top = topBarPadding.calculateTopPadding(),
                    bottom = bottomPadding.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val isBannerVisibleFlow = remember { mutableStateOf(false) }
            val bannerAdViewFlow by remember {
                mutableStateOf(
                    adViewModel.loadBannerAd() { adState ->
                        isBannerVisibleFlow.value = adState.bannerAdView != null
                    }
                )
            }


            AnimatedVisibility(
                visible = isBannerVisibleFlow.value,
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg)),
                    factory = { bannerAdViewFlow }
                )
            }


            val scrollState = rememberScrollState()
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    //.background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                    .padding(horizontal = dimensionResource(id = R.dimen.padding))
                    .padding(
                        // top = 50.dp,
                        bottom = bottomPadding.calculateBottomPadding()
                    ),
                verticalArrangement = Arrangement.spacedBy(25.dp),
            ) {

                OverviewTopBarProfile(
                    onClick = {},
                    user = userData,
                    isSubscribed = false,
                    onSubscriptionChanged = overViewViewModel::onSubscriptionChanged
                )

                OverviewData(
                    currentPeriod = currentPeriod,
                    data = currentTotal,
                    //currency = currency,
                    categoryList = currentMonthCategory,
                    recentTransaction = recentTransaction,
                    recentMerchant = recentMerchant,
                    onSeeAllTransactionClick = {
                        adViewModel.showInterstitialAd(context as android.app.Activity) { onSeeAllTransactionClick() }
                    },
                    onSeeAllMerchantClick = {
                        adViewModel.showInterstitialAd(context as android.app.Activity) { onSeeAllMerchantClick() }
                    },
                    onExploreAnalysisClick = {
                        adViewModel.showInterstitialAd(context as android.app.Activity) { onExploreAnalysisClick() }
                    },
                    onExploreBudgetClick = {
                        adViewModel.showInterstitialAd(context as android.app.Activity) { onExploreBudgetClick() }
                    },
                    onTransactionClick = { id ->
                        adViewModel.showInterstitialAd(context as android.app.Activity) {
                            onTransactionClick(id)
                        }
                    },
                    merchantDataAnimId = currentMerchantDataAnimId,
                    merchantDataAnimType = currentMerchantDataAnim,
                    onMerchantDataAnimStop = {
                        overViewViewModel.onMerchantDataAnimationComplete(it)
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
                        overViewViewModel.onMerchantAnimationComplete(it)
                    },
                    merchantAnim = currentMerchantAnim,
                    merchantAnimId = currentMerchantAnimId,
                    onSetBudgetClick = { id ->
                        adViewModel.showInterstitialAd(context as android.app.Activity) {
                            onSetBudgetClick(
                                id
                            )
                        }
                    },
                    onMerchantClick = { id ->
                        adViewModel.showInterstitialAd(context as android.app.Activity) {
                            onMerchantClick(
                                id
                            )
                        }
                    },
                    isSelectionEnable = budgetState.count { it.periodType == PeriodType.MONTH.id || it.periodType == PeriodType.YEAR.id } == 2
                )

                Spacer(modifier = Modifier.height(30.dp))
                //}
            }

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