package com.indie.apps.pennypal.presentation.ui.screen.add_budget

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.enum.BudgetPeriodType
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomDatePickerDialog
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomMonthPickerDialog
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomYearPickerDialog
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.getDateFromMillis
import java.text.SimpleDateFormat
import java.util.Calendar

@SuppressLint("StateFlowValueCalledInComposition", "SimpleDateFormat")
@Composable
fun AddBudgetScreen(
    addBudgetViewModel: AddBudgetViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSave: (Boolean, Long) -> Unit,
    selectedCategoryIds: List<Long> = emptyList(),
    onSelectCategory: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val uiState by addBudgetViewModel.uiState.collectAsStateWithLifecycle()
    var openFromDateDialog by remember { mutableStateOf(false) }
    var openToDateDialog by remember { mutableStateOf(false) }
    var openYearDialog by remember { mutableStateOf(false) }
    var openMonthDialog by remember { mutableStateOf(false) }

    var isCategoryExpanded by remember { mutableStateOf(false) }

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

    LaunchedEffect(selectedCategoryIds) {
        addBudgetViewModel.setSelectedCategory(selectedCategoryIds)
    }

    var openDiscardDialog by remember { mutableStateOf(false) }

    BackHandler {
        if (addBudgetViewModel.isEditData()) {
            openDiscardDialog = true
        } else {
            onNavigationUp()
        }
    }

    when (uiState) {
        is Resource.Loading -> {
            LoadingWithProgress()
        }

        is Resource.Success -> {
            val title = if (addBudgetViewModel.budgetEditId == 0L)
                stringResource(id = R.string.add_budget)
            else
                stringResource(id = R.string.edit_Budget)
            Scaffold(
                topBar = {
                    TopBarWithTitle(
                        title = title,
                        onNavigationUp = {
                            if (addBudgetViewModel.isEditData()) {
                                openDiscardDialog = true
                            } else {
                                onNavigationUp()
                            }
                        }, contentAlignment = Alignment.Center
                    )
                }
            ) { innerPadding ->

                val scrollState = rememberScrollState()

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding))
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    val dateFormat = SimpleDateFormat("dd MMM yyyy")
                    val yearFormat = SimpleDateFormat("yyyy")
                    val monthFormat = SimpleDateFormat("MMMM yyyy")

                    AddBudgetTopSelectionButton(
                        list = BudgetPeriodType.entries,
                        selectBudgetPeriod = BudgetPeriodType.entries.first { it.id == currentPeriod },
                        onSelect = addBudgetViewModel::setCurrentPeriod,
                    )

                    AddBudgetFieldItem(
                        onSelectCategory = onSelectCategory,
                        selectBudgetPeriod = BudgetPeriodType.entries.first { it.id == currentPeriod },
                        onSelectFromDate = {
                            openFromDateDialog = true
                        },
                        onSelectMonth = {
                            openMonthDialog = true
                        },
                        onSelectYear = {
                            openYearDialog = true
                        },
                        onSelectToDate = {
                            openToDateDialog = true
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
                        periodErrorText = periodErrorText,
                        periodFromErrorText = periodFromErrorText,
                        periodToErrorText = periodToErrorText,
                        categoryErrorText = categoryErrorText,
                        categoryBudgetErrorText = categoryBudgetErrorText,
                        onAmountTextChange = addBudgetViewModel::updateAmountText,
                        budgetTitle = budgetTitle,
                        onBudgetTitleTextChange = addBudgetViewModel::updateBudgetTitleText
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    BottomSaveButton(
                        onClick = {
                            addBudgetViewModel.saveData { isEdit, id ->
                                onSave(isEdit, id)
                            }
                        },
                        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding))
                    )
                }
            }
        }

        is Resource.Error -> {
            LoadingWithProgress()
        }
    }

    if (openFromDateDialog) {
        CustomDatePickerDialog(
            currentTimeInMilli = if (currentFromTimeInMilli == 0L) Calendar.getInstance().timeInMillis else currentFromTimeInMilli,
            onDateSelected = {
                openFromDateDialog = false
                addBudgetViewModel.setCurrentFromTime(it.timeInMillis)
            },
            onDismiss = {
                openFromDateDialog = false
            }
        )
    } else if (openToDateDialog) {
        CustomDatePickerDialog(
            currentTimeInMilli = if (currentToTimeInMilli == 0L) Calendar.getInstance().timeInMillis else currentToTimeInMilli,
            onDateSelected = {
                openToDateDialog = false
                addBudgetViewModel.setCurrentToTime(it.timeInMillis)
            },
            onDismiss = {
                openToDateDialog = false
            }
        )
    } else if (openMonthDialog) {
        CustomMonthPickerDialog(
            currentYear = Calendar.getInstance().apply { timeInMillis = currentMonthInMilli }
                .get(Calendar.YEAR),
            currentMonth = Calendar.getInstance().apply { timeInMillis = currentMonthInMilli }
                .get(Calendar.MONTH),
            onDismiss = {
                openMonthDialog = false
            },
            onDateSelected = { year, month ->
                openMonthDialog = false
                addBudgetViewModel.setCurrentMonth(year = year, month = month)
            }
        )
    } else if (openYearDialog) {
        CustomYearPickerDialog(
            currentYear = Calendar.getInstance().apply { timeInMillis = currentYearInMilli }
                .get(Calendar.YEAR),
            onDismiss = {
                openYearDialog = false
            },
            onDateSelected = {
                openYearDialog = false
                addBudgetViewModel.setCurrentYear(it)
            }
        )
    }

    if (openDiscardDialog) {
        ConfirmationDialog(
            dialogTitle = R.string.discard_dialog_title,
            dialogText = R.string.discard_dialog_text,
            onConfirmation = {
                openDiscardDialog = false
                onNavigationUp()
            },
            onDismissRequest = {
                openDiscardDialog = false
            },
            positiveText = R.string.discard,
            negativeText = R.string.cancel
        )
    }

}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        AddBudgetScreen(
            onNavigationUp = {},
            onSelectCategory = {},
            onSave = { _, _ -> }
        )
    }
}