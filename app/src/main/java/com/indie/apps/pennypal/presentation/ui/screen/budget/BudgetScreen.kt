package com.indie.apps.pennypal.presentation.ui.screen.budget

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.enum.DialogType
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomMonthPickerDialog
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomYearPickerDialog
import com.indie.apps.pennypal.presentation.ui.screen.add_budget.AddBudgetTopSelectionButton
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import java.util.Calendar

@SuppressLint("StateFlowValueCalledInComposition", "SimpleDateFormat")
@Composable
fun BudgetScreen(
    budgetViewModel: BudgetViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onAddClick: (Int) -> Unit,
    onBudgetEditClick: (Long) -> Unit,
    onBudgetMenuClick: (Int) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val title = stringResource(id = R.string.budget_analysis)

    val currency by budgetViewModel.currency.collectAsStateWithLifecycle()
    val budgetState by budgetViewModel.budgetState.collectAsStateWithLifecycle()
    val currentYearInMilli by remember { budgetViewModel.currentYearInMilli }.collectAsStateWithLifecycle()
    val currentMonthInMilli by remember { budgetViewModel.currentMonthInMilli }.collectAsStateWithLifecycle()

    val isExpandOneTimeUpcomingData by remember { budgetViewModel.isExpandOneTimeUpcomingData }.collectAsStateWithLifecycle()
    val isExpandOneTimePastData by remember { budgetViewModel.isExpandOneTimePastData }.collectAsStateWithLifecycle()
    val isExpandOneTimeActiveData by remember { budgetViewModel.isExpandOneTimeActiveData }.collectAsStateWithLifecycle()

    val pagedDataUpcomingBudget = budgetViewModel.pagedDataUpcomingBudget.collectAsLazyPagingItems()
    val pagedDataPastBudget = budgetViewModel.pagedDataPastBudget.collectAsLazyPagingItems()

    var openDialog by remember { mutableStateOf<DialogType?>(null) }

    val currentPeriod by budgetViewModel.currentPeriod.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            BudgetTopBar(
                title = title,
                onNavigationUp = onNavigationUp,
                onAddClick = { onAddClick(currentPeriod.id) }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding)
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
                    BudgetList(budgetState, currency, onBudgetEditClick)
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
                        currency = currency,
                        onBudgetEditClick = onBudgetEditClick
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
                        currency = currency,
                        onBudgetEditClick = onBudgetEditClick
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
                        currency = currency,
                        onBudgetEditClick = onBudgetEditClick
                    )
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
            onBudgetMenuClick = {}
        )
    }
}