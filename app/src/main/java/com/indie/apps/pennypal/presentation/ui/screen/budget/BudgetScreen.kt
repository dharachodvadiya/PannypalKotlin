package com.indie.apps.pennypal.presentation.ui.screen.budget

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomMonthPickerDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomYearPickerDialog
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.screen.add_budget.AddBudgetTopSelectionButton
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.ads.AdViewModel
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.feedback.InAppFeedbackViewModel
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.app_enum.DialogType
import com.indie.apps.pennypal.util.app_enum.PeriodType
import java.util.Calendar

@SuppressLint("StateFlowValueCalledInComposition", "SimpleDateFormat")
@Composable
fun BudgetScreen(
    budgetViewModel: BudgetViewModel = hiltViewModel(),
    inAppFeedbackViewModel: InAppFeedbackViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onAddClick: (Int) -> Unit,
    onBudgetEditClick: (Long) -> Unit,
    onBudgetMenuClick: (Int) -> Unit,
    isAddSuccess: Boolean = false,
    isDeleteSuccess: Boolean = false,
    budgetId: Long = -1L,
    currentYear: Int = -1,
    currentMonth: Int = -1,
    periodType: Int?,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Load ad when screen is created
    LaunchedEffect(Unit) {
        adViewModel.loadInterstitialAd()
    }

    val title = stringResource(id = R.string.budget_analysis)

    //val currency by budgetViewModel.currency.collectAsStateWithLifecycle()
    val budgetState by budgetViewModel.budgetState.collectAsStateWithLifecycle()
    val currentYearInMilli by remember { budgetViewModel.currentYearInMilli }.collectAsStateWithLifecycle()
    val currentMonthInMilli by remember { budgetViewModel.currentMonthInMilli }.collectAsStateWithLifecycle()

    val isExpandOneTimeUpcomingData by remember { budgetViewModel.isExpandOneTimeUpcomingData }.collectAsStateWithLifecycle()
    val isExpandOneTimePastData by remember { budgetViewModel.isExpandOneTimePastData }.collectAsStateWithLifecycle()
    val isExpandOneTimeActiveData by remember { budgetViewModel.isExpandOneTimeActiveData }.collectAsStateWithLifecycle()

    val pagedDataUpcomingBudget = budgetViewModel.pagedDataUpcomingBudget.collectAsLazyPagingItems()
    val pagedDataPastBudget = budgetViewModel.pagedDataPastBudget.collectAsLazyPagingItems()

    val currentAnim by budgetViewModel.currentAnim.collectAsStateWithLifecycle()
    val budgetAnimId by budgetViewModel.currentAnimId.collectAsStateWithLifecycle()

    var openDialog by remember { mutableStateOf<DialogType?>(null) }

    val currentPeriod by budgetViewModel.currentPeriod.collectAsStateWithLifecycle()

    //val context = LocalContext.current
    val budgetDeleteToast = stringResource(id = R.string.budget_delete_success_toast)

    LaunchedEffect(budgetId) {
        if (isAddSuccess) {
            //addMerchantId = editAddId
            budgetViewModel.addBudgetSuccess(budgetId)
            inAppFeedbackViewModel.triggerReview(context)
        } else if (isDeleteSuccess) {
            budgetViewModel.onDeleteFromEditScreenClick(budgetId) {
                context.showToast(budgetDeleteToast)
            }
        }
    }


    LaunchedEffect(periodType) {
        if (periodType != null) {
            PeriodType.fromId(periodType)
                ?.let { budgetViewModel.setCurrentPeriodWithTime(it, currentMonth, currentYear) }
        }
    }

    Scaffold(
        topBar = {
            BudgetTopBar(
                title = title,
                onNavigationUp = onNavigationUp,
                onAddClick = {
                    budgetViewModel.logEvent("budget_add")
                    onAddClick(currentPeriod.id)
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding),
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
                    factory = { bannerAdViewFlow!! }
                )
            }

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    //.background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                    // .padding(innerPadding)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding)),
                //verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Budget Period Selection
                item {
                    AddBudgetTopSelectionButton(
                        list = PeriodType.entries,
                        selectBudgetPeriod = PeriodType.entries.first { it.id == currentPeriod.id },
                        onSelect = { budgetViewModel.setCurrentPeriod(it) }
                    )
                }

                // Month/Year Selection and Budgets for MONTH/YEAR
                if (currentPeriod != PeriodType.ONE_TIME) {
                    item {
                        Spacer(Modifier.height(20.dp))
                        MonthYearSelection(
                            currentPeriod,
                            currentMonthInMilli,
                            currentYearInMilli,
                            budgetViewModel,
                            onOpenDialog = { openDialog = it })
                    }
                    item {
                        Spacer(Modifier.height(20.dp))
                        BudgetList(
                            budgetState = budgetState,
                            onBudgetEditClick = { id ->
                                adViewModel.showInterstitialAd(context as android.app.Activity) {
                                    budgetViewModel.logEvent("budget_edit_month_year")
                                    onBudgetEditClick(id)
                                }
                            },
                            currentAnim = currentAnim,
                            budgetAnimId = budgetAnimId,
                            onAnimComplete = {
                                budgetViewModel.onAnimationComplete(it)
                            })
                    }
                } else {
                    // ONE_TIME Budgets
                    if (budgetState.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(20.dp))
                        }
                        expandableBudgetGroup(
                            title = R.string.active_budget,
                            isExpanded = isExpandOneTimeActiveData,
                            onExpandClick = budgetViewModel::onActiveExpandClick,
                            items = budgetState,
                            //currency = currency,
                            onBudgetEditClick = { id ->
                                adViewModel.showInterstitialAd(context as android.app.Activity) {
                                    budgetViewModel.logEvent("budget_edit_active")
                                    onBudgetEditClick(id)
                                }
                            },
                            currentAnim = currentAnim,
                            budgetAnimId = budgetAnimId,
                            onAnimationComplete = {
                                budgetViewModel.onAnimationComplete(it)
                            }
                        )
                    }
                    if (pagedDataUpcomingBudget.itemCount > 0) {
                        item {
                            Spacer(Modifier.height(20.dp))
                        }
                        expandableBudgetGroup(
                            title = R.string.upcoming_boudget,
                            isExpanded = isExpandOneTimeUpcomingData,
                            onExpandClick = budgetViewModel::onUpcomingExpandClick,
                            pagingItems = pagedDataUpcomingBudget,
                            //currency = currency,
                            onBudgetEditClick = { id ->
                                adViewModel.showInterstitialAd(context as android.app.Activity) {
                                    budgetViewModel.logEvent("budget_edit_upcoming")
                                    onBudgetEditClick(id)
                                }
                            },
                            currentAnim = currentAnim,
                            budgetAnimId = budgetAnimId,
                            onAnimationComplete = {
                                budgetViewModel.onAnimationComplete(it)
                            }
                        )
                    }
                    if (pagedDataPastBudget.itemCount > 0) {
                        item {
                            Spacer(Modifier.height(20.dp))
                        }
                        expandableBudgetGroup(
                            title = R.string.past_boudget,
                            isExpanded = isExpandOneTimePastData,
                            onExpandClick = budgetViewModel::onPastExpandClick,
                            pagingItems = pagedDataPastBudget,
                            //currency = currency,
                            onBudgetEditClick = { id ->
                                adViewModel.showInterstitialAd(context as android.app.Activity) {
                                    budgetViewModel.logEvent("budget_edit_past")
                                    onBudgetEditClick(id)
                                }
                            },
                            currentAnim = currentAnim,
                            budgetAnimId = budgetAnimId,
                            onAnimationComplete = {
                                budgetViewModel.onAnimationComplete(it)
                            }
                        )
                    }
                }
            }
        }


    }

    // Dialog Handling
    openDialog?.let { dialog ->
        when (dialog) {
            DialogType.Month -> CustomMonthPickerDialog(
                currentYear = Calendar.getInstance().apply { timeInMillis = currentMonthInMilli }
                    .get(Calendar.YEAR),
                currentMonth = Calendar.getInstance().apply { timeInMillis = currentMonthInMilli }
                    .get(Calendar.MONTH),
                onDismiss = { openDialog = null },
                onDateSelected = { year, month ->
                    openDialog = null
                    budgetViewModel.setCurrentMonth(year, month)
                }
            )

            DialogType.Year -> CustomYearPickerDialog(
                currentYear = Calendar.getInstance().apply { timeInMillis = currentYearInMilli }
                    .get(Calendar.YEAR),
                onDismiss = { openDialog = null },
                onDateSelected = {
                    openDialog = null
                    budgetViewModel.setCurrentYear(it)
                }
            )

            else -> {}
        }
    }

}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        BudgetScreen(
            onNavigationUp = {},
            onAddClick = {},
            onBudgetEditClick = {},
            onBudgetMenuClick = {},
            periodType = null
        )
    }
}