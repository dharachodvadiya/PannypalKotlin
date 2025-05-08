package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
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
import com.indie.apps.pennypal.presentation.ui.component.composable.common.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.composable.common.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomMonthPickerDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomYearPickerDialog
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.screen.AdViewModel
import com.indie.apps.pennypal.presentation.ui.screen.InAppFeedbackViewModel
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.app_enum.AnalysisPeriod
import com.indie.apps.pennypal.util.app_enum.DialogType
import com.indie.apps.pennypal.util.app_enum.Resource
import java.util.Calendar

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun OverViewAnalysisScreen(
    viewModel: OverViewAnalysisViewModel = hiltViewModel(),
    inAppFeedbackViewModel: InAppFeedbackViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    bottomPadding: PaddingValues,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val localContext = LocalContext.current

    // Load ad when screen is created
    LaunchedEffect(Unit) {
        adViewModel.loadInterstitialAd()
    }

    //val currency by viewModel.currency.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val currentPeriod by remember { viewModel.currentPeriod }.collectAsStateWithLifecycle()
    val currentYearInMilli by remember { viewModel.currentYearInMilli }.collectAsStateWithLifecycle()
    val currentMonthInMilli by remember { viewModel.currentMonthInMilli }.collectAsStateWithLifecycle()
    val categoryExpense by remember { viewModel.categoryExpense }.collectAsStateWithLifecycle()
    val currentTotal by remember { viewModel.currentTotal }.collectAsStateWithLifecycle()
    var openDialog by remember { mutableStateOf<DialogType?>(null) }
    val title = stringResource(id = R.string.analysis)

    Scaffold(
        topBar = {
            TopBarWithTitle(
                title = title,
                onNavigationUp = {
                    onNavigationUp()
                }, contentAlignment = Alignment.Center
            )
        }
    ) { innerPadding ->

        //val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(id = R.dimen.padding)),
            // .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
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

            AnalysisTopSelectionButton(
                list = AnalysisPeriod.entries,
                selectedPeriod = currentPeriod,
                onSelect = { period ->
                    adViewModel.showInterstitialAd(
                        localContext as android.app.Activity,
                        isReload = true
                    ) {
                        viewModel.setCurrentPeriod(
                            period
                        )
                    }
                },
            )

            val timeInMilli = when (currentPeriod) {
                AnalysisPeriod.MONTH -> currentMonthInMilli
                AnalysisPeriod.YEAR -> currentYearInMilli
            }

            AnalysisMonthYearSelection(
                textYearMonth = viewModel.formatDateForDisplay(currentPeriod, timeInMilli),
                onPreviousClick = {
                    adViewModel.showInterstitialAd(
                        localContext as android.app.Activity,
                        isReload = true
                    ) {
                        viewModel.onPreviousClick()
                    }
                },
                onNextClick = {
                    adViewModel.showInterstitialAd(
                        localContext as android.app.Activity,
                        isReload = true
                    ) {
                        viewModel.onNextClick()
                    }
                },
                onTextClick = {
                    if (isLoading)
                        return@AnalysisMonthYearSelection
                    when (currentPeriod) {
                        AnalysisPeriod.MONTH -> openDialog = DialogType.Month
                        AnalysisPeriod.YEAR -> openDialog = DialogType.Year
                    }
                }
            )
            //PeriodText(currentPeriod?.let { stringResource(currentPeriod!!.title) } ?: "")

            when {

                isLoading -> {
                    LoadingWithProgress(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                categoryExpense is Resource.Error || currentTotal is Resource.Error -> {
                    LoadingWithProgress(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                categoryExpense is Resource.Success && currentTotal is Resource.Success -> {

                    val scrollState = rememberScrollState()
                    val dataList = categoryExpense.data ?: emptyList()

                    if (dataList.isEmpty()) {
                        NoDataMessage(
                            title = stringResource(id = R.string.no_transaction),
                            details = "",
                            iconSize = 70.dp
                        )
                    } else {

                        LaunchedEffect(Unit) {
                            inAppFeedbackViewModel.triggerReview(localContext, true)
                        }

                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(bottom = bottomPadding.calculateBottomPadding()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            AnalysisBalance(
                                spentAmount = currentTotal.data?.totalExpense ?: 0.0,
                                receiveAmount = currentTotal.data?.totalIncome ?: 0.0,
                                currency = currentTotal.data?.baseCurrencySymbol ?: ""
                            )

                            OverViewAnalysisCategoryChart(
                                categoryList = dataList,
                                //currency = currency
                            )
                        }
                    }
                }
            }


        }
    }

    openDialog?.let { dialog ->
        when (dialog) {
            DialogType.Month -> {
                CustomMonthPickerDialog(
                    currentYear = Calendar.getInstance()
                        .apply { timeInMillis = currentMonthInMilli }
                        .get(Calendar.YEAR),
                    currentMonth = Calendar.getInstance()
                        .apply { timeInMillis = currentMonthInMilli }
                        .get(Calendar.MONTH),
                    onDismiss = {
                        openDialog = null
                    },
                    onDateSelected = { year, month ->
                        openDialog = null
                        viewModel.setCurrentMonth(year = year, month = month)
                    }
                )
            }

            DialogType.Year -> {
                CustomYearPickerDialog(
                    currentYear = Calendar.getInstance().apply { timeInMillis = currentYearInMilli }
                        .get(Calendar.YEAR),
                    onDismiss = {
                        openDialog = null
                    },
                    onDateSelected = {
                        openDialog = null
                        viewModel.setCurrentYear(it)
                    }
                )
            }

            else -> {}
        }
    }

}

@Preview
@Composable
private fun OverViewScreenPreview() {
    /* PennyPalTheme(darkTheme = true) {
         OverViewAnalysisScreen(
             onNavigationUp = {},
         )
     }*/
}