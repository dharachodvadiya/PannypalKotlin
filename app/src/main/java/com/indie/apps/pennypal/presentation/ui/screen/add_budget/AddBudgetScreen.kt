package com.indie.apps.pennypal.presentation.ui.screen.add_budget

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.common.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.composable.common.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomDatePickerDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomMonthPickerDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomYearPickerDialog
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.ads.AdViewModel
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.app_enum.DialogType
import com.indie.apps.pennypal.util.app_enum.PeriodType
import com.indie.apps.pennypal.util.app_enum.Resource
import com.indie.apps.pennypal.util.internanal.method.getDateFromMillis
import java.text.SimpleDateFormat
import java.util.Calendar

@SuppressLint("StateFlowValueCalledInComposition", "SimpleDateFormat")
@Composable
fun AddEditBudgetScreen(
    addBudgetViewModel: AddBudgetViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel(),
    onCurrencyChange: (String) -> Unit,
    onNavigationUp: () -> Unit,
    onSave: (Boolean, Long, Int, Int, Int) -> Unit,
    selectedCategoryIds: List<Long> = emptyList(),
    onSelectCategory: (List<Long>) -> Unit,
    selectedPeriodType: Int,
    currencyCountryCode: String? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    // Load ad when screen is created
    LaunchedEffect(Unit) {
        adViewModel.loadInterstitialAd()
    }
    val uiState by addBudgetViewModel.uiState.collectAsStateWithLifecycle()

    var isCategoryExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedCategoryIds) {
        addBudgetViewModel.setSelectedCategory(selectedCategoryIds)
    }

    LaunchedEffect(selectedPeriodType) {
        addBudgetViewModel.setSelectPeriodType(selectedPeriodType)
    }

    LaunchedEffect(currencyCountryCode) {
        if (currencyCountryCode != null) {
            addBudgetViewModel.setCurrencyCountryCode(currencyCountryCode)
        }
    }

    //val currency by addBudgetViewModel.currency.collectAsStateWithLifecycle()
    val originalCurrencyInfo by addBudgetViewModel.originalCurrencyInfo.collectAsStateWithLifecycle()
    val currentPeriod by addBudgetViewModel.currentPeriod.collectAsStateWithLifecycle()
    val currentMonthInMilli by addBudgetViewModel.currentMonthInMilli.collectAsStateWithLifecycle()
    val currentYearInMilli by addBudgetViewModel.currentYearInMilli.collectAsStateWithLifecycle()
    val currentFromTimeInMilli by addBudgetViewModel.currentFromTimeInMilli.collectAsStateWithLifecycle()
    val currentToTimeInMilli by addBudgetViewModel.currentToTimeInMilli.collectAsStateWithLifecycle()
    val amount by addBudgetViewModel.amount.collectAsStateWithLifecycle()
    val budgetTitle by addBudgetViewModel.budgetTitle.collectAsStateWithLifecycle()
    val remainingAmount by addBudgetViewModel.remainingAmount.collectAsStateWithLifecycle()
    val periodErrorText by addBudgetViewModel.periodErrorText.collectAsStateWithLifecycle()
    val periodFromErrorText by addBudgetViewModel.periodFromErrorText.collectAsStateWithLifecycle()
    val periodToErrorText by addBudgetViewModel.periodToErrorText.collectAsStateWithLifecycle()
    val categoryErrorText by addBudgetViewModel.categoryErrorText.collectAsStateWithLifecycle()
    val categoryBudgetErrorText by addBudgetViewModel.categoryBudgetErrorText.collectAsStateWithLifecycle()
    val selectedCategoryList by addBudgetViewModel.selectedCategoryList.collectAsStateWithLifecycle()


    //val context = LocalContext.current
    val budgetEditToast = stringResource(id = R.string.budget_edit_success_toast)
    val budgetAddToast = stringResource(id = R.string.budget_add_success_toast)
    val periodEditToast = stringResource(id = R.string.period_not_edit_toast)

    var openDialog by remember { mutableStateOf<DialogType?>(null) }

    val focusRequesterTitle = remember { FocusRequester() }
    val focusRequesterAmount = remember { FocusRequester() }

    BackHandler {
        if (addBudgetViewModel.isEditData()) {
            openDialog = DialogType.Discard
        } else {
            onNavigationUp()
        }
    }

    when (uiState) {
        is Resource.Loading -> {
            LoadingWithProgress()
        }

        is Resource.Success -> {

            LaunchedEffect(Unit) {
                if (!addBudgetViewModel.isEditData())
                    focusRequesterTitle.requestFocus()
            }


            val title = if (addBudgetViewModel.budgetEditId == -1L)
                stringResource(id = R.string.add_budget)
            else
                stringResource(id = R.string.edit_Budget)
            Scaffold(
                topBar = {
                    TopBarWithTitle(
                        title = title,
                        onNavigationUp = {
                            if (addBudgetViewModel.isEditData()) {
                                openDialog = DialogType.Discard
                            } else {
                                onNavigationUp()
                            }
                        }, contentAlignment = Alignment.Center
                    )
                }
            ) { innerPadding ->

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                        .padding(innerPadding)
                        //.padding(horizontal = dimensionResource(id = R.dimen.padding))
                        .imePadding(),
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
                            factory = { bannerAdViewFlow!! }
                        )
                    }

                    val scrollState = rememberScrollState()

                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            //.background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                            //.padding(innerPadding)
                            .padding(horizontal = dimensionResource(id = R.dimen.padding))
                            //.imePadding()
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        val dateFormat = SimpleDateFormat("dd MMM yyyy")
                        val yearFormat = SimpleDateFormat("yyyy")
                        val monthFormat = SimpleDateFormat("MMMM yyyy")

                        if (!addBudgetViewModel.isEditData()) {
                            AddBudgetTopSelectionButton(
                                list = PeriodType.entries,
                                selectBudgetPeriod = PeriodType.entries.firstOrNull { it.id == currentPeriod }
                                    ?: PeriodType.MONTH,
                                onSelect = addBudgetViewModel::setCurrentPeriod,
                            )
                        }
                        AddBudgetFieldItem(
                            onSelectCategory = {
                                onSelectCategory(selectedCategoryList.map { it.id })
                            },
                            selectBudgetPeriod = PeriodType.entries.firstOrNull() { it.id == currentPeriod }
                                ?: PeriodType.MONTH,
                            onSelectFromDate = {
                                openDialog = DialogType.FromDate
                            },
                            onSelectMonth = {
                                if (!addBudgetViewModel.isEditData())
                                    openDialog = DialogType.Month
                                else
                                    context.showToast(periodEditToast)
                            },
                            onSelectYear = {
                                if (!addBudgetViewModel.isEditData())
                                    openDialog = DialogType.Year
                                else
                                    context.showToast(periodEditToast)
                            },
                            onSelectToDate = {
                                openDialog = DialogType.ToDate
                            },
                            amount = amount,
                            categoryList = selectedCategoryList,
                            isExpanded = isCategoryExpanded,
                            onCategoryAmountChange = addBudgetViewModel::setCategoryAmount,
                            onCategoryExpandChange = { isCategoryExpanded = it },
                            totalCategoryAmount = amount.text.toDoubleOrNull() ?: 0.0,
                            remainingCategoryAmount = remainingAmount,
                            currentMonth = getDateFromMillis(currentMonthInMilli, monthFormat),
                            currentYear = getDateFromMillis(currentYearInMilli, yearFormat),
                            currentFromDate = getDateFromMillis(currentFromTimeInMilli, dateFormat),
                            currentToDate = getDateFromMillis(currentToTimeInMilli, dateFormat),
                            periodErrorText = periodErrorText.asString(),
                            periodFromErrorText = periodFromErrorText.asString(),
                            periodToErrorText = periodToErrorText.asString(),
                            categoryErrorText = categoryErrorText.asString(),
                            categoryBudgetErrorText = categoryBudgetErrorText.asString(),
                            onAmountTextChange = addBudgetViewModel::updateAmountText,
                            budgetTitle = budgetTitle,
                            onBudgetTitleTextChange = addBudgetViewModel::updateBudgetTitleText,
                            focusRequesterAmount = focusRequesterAmount,
                            focusRequesterTitle = focusRequesterTitle,
                            currency = originalCurrencyInfo?.currencySymbol ?: "$",
                            onCurrencyChange = {
                                onCurrencyChange(originalCurrencyInfo?.currencyCountryCode ?: "US")
                            }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        BottomSaveButton(
                            onClick = {
                                addBudgetViewModel.saveData { isEdit, id, month, year ->

                                    adViewModel.showInterstitialAd(context as android.app.Activity) {
                                        onSave(isEdit, id, currentPeriod, month, year)
                                        if (isEdit) {
                                            context.showToast(budgetEditToast)
                                        } else {
                                            context.showToast(budgetAddToast)
                                        }
                                    }

                                }
                            },
                            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding))
                        )
                    }
                }
            }
        }

        is Resource.Error -> {
            LoadingWithProgress()
        }
    }

    openDialog?.let { dialog ->
        when (dialog) {
            DialogType.Discard -> {
                ConfirmationDialog(
                    dialogTitle = R.string.discard_dialog_title,
                    dialogText = R.string.discard_dialog_text,
                    onConfirmation = {
                        openDialog = null
                        onNavigationUp()
                    },
                    onDismissRequest = {
                        openDialog = null
                    },
                    positiveText = R.string.discard,
                    negativeText = R.string.cancel
                )
            }

            DialogType.FromDate -> {
                CustomDatePickerDialog(
                    currentTimeInMilli = if (currentFromTimeInMilli == 0L) Calendar.getInstance().timeInMillis else currentFromTimeInMilli,
                    onDateSelected = {
                        openDialog = null
                        addBudgetViewModel.setCurrentFromTime(it.timeInMillis)
                    },
                    onDismiss = {
                        openDialog = null
                    }
                )
            }

            DialogType.ToDate -> {
                CustomDatePickerDialog(
                    currentTimeInMilli = if (currentToTimeInMilli == 0L) Calendar.getInstance().timeInMillis else currentToTimeInMilli,
                    onDateSelected = {
                        openDialog = null
                        addBudgetViewModel.setCurrentToTime(it.timeInMillis)
                    },
                    onDismiss = {
                        openDialog = null
                    }
                )
            }

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
                        addBudgetViewModel.setCurrentMonth(year = year, month = month)
                        openDialog = null
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
                        addBudgetViewModel.setCurrentYear(it)
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
    PennyPalTheme(darkTheme = true) {
        AddEditBudgetScreen(
            onNavigationUp = {},
            onSelectCategory = {},
            onSave = { _, _, _, _, _ -> },
            selectedPeriodType = 1,
            onCurrencyChange = {}
        )
    }
}