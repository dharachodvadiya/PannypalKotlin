package com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.presentation.ui.component.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.screen.InAppFeedbackViewModel
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.getDateFromMillis
import java.text.SimpleDateFormat

@SuppressLint("StateFlowValueCalledInComposition", "SimpleDateFormat")
@Composable
fun SingleBudgetScreen(
    viewModel: SingleBudgetViewModel = hiltViewModel(),
    inAppFeedbackViewModel: InAppFeedbackViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onDeleteSuccess: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val title = stringResource(id = R.string.budget_analysis)
    val context = LocalContext.current

    var openAlertDialog by remember { mutableStateOf(false) }

    // val currency by viewModel.currency.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val budgetData by viewModel.budgetData.collectAsStateWithLifecycle()
    val spentCategoryData by viewModel.spentCategoryData.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadBudgetData()
    }
    when (uiState) {
        is Resource.Loading -> {
            LoadingWithProgress()
        }

        is Resource.Success -> {
            Scaffold(
                topBar = {
                    SingleBudgetTopBar(
                        title = title,
                        onNavigationUp = onNavigationUp,
                        onDeleteClick = {
                            openAlertDialog = true
                        },
                        onEditClick = {
                            budgetData?.let { onEditClick(it.id) }
                        }
                    )
                }
            ) { innerPadding ->

                LaunchedEffect(Unit) {
                    inAppFeedbackViewModel.triggerReview(context, true)
                }

                val scrollState = rememberScrollState()

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding))
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    val dateFormat = SimpleDateFormat("dd MMM yyyy")
                    val yearFormat = SimpleDateFormat("yyyy")
                    val monthFormat = SimpleDateFormat("MMMM yyyy")

                    val timeString = budgetData?.let { tmpBudgetData ->
                        when (tmpBudgetData.periodType) {
                            PeriodType.MONTH.id -> getDateFromMillis(
                                tmpBudgetData.startDate,
                                monthFormat
                            )

                            PeriodType.YEAR.id -> getDateFromMillis(
                                tmpBudgetData.startDate,
                                yearFormat
                            )

                            PeriodType.ONE_TIME.id -> "${
                                getDateFromMillis(
                                    tmpBudgetData.startDate,
                                    dateFormat
                                )
                            } - ${
                                tmpBudgetData.endDate?.let { date ->
                                    getDateFromMillis(
                                        date, dateFormat
                                    )
                                }
                            }"

                            else -> ""
                        }
                    } ?: ""

                    SingleBudgetOverAllAnalysis(
                        totalBudgetAmount = budgetData?.amount ?: 0.0,
                        spentAmount = spentCategoryData.sumOf { it.amount },
                        timeString = timeString,
                        currency = budgetData?.originalAmountSymbol ?: "$"
                    )

                    BudgetedCategoryAnalysis(
                        categoryLimitList = budgetData?.category ?: emptyList(),
                        categorySpentList = spentCategoryData,
                        currency = budgetData?.originalAmountSymbol ?: "$"
                    )

                    IncludedCategoryAnalysis(
                        categoryLimitList = budgetData?.category ?: emptyList(),
                        categorySpentList = spentCategoryData,
                        currency = budgetData?.originalAmountSymbol ?: "$"
                    )
                }
            }
        }

        is Resource.Error -> {
            LoadingWithProgress()
        }
    }
    val budgetDeleteToast = stringResource(id = R.string.budget_delete_success_toast)
    if (openAlertDialog) {
        ConfirmationDialog(
            dialogTitle = R.string.delete_dialog_title,
            dialogText = R.string.delete_item_dialog_text,
            onConfirmation = {
                viewModel.onDeleteDialogClick {
                    openAlertDialog = false
                   // context.showToast(budgetDeleteToast)
                    onDeleteSuccess(it)
                }
            },
            onDismissRequest = { openAlertDialog = false }
        )
    }

}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        SingleBudgetScreen(
            onNavigationUp = {},
            onEditClick = {},
            onDeleteSuccess = {
            }
        )
    }
}