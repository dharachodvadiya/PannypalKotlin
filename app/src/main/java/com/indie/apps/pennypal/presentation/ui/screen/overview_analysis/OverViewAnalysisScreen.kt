package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.enum.AnalysisPeriod
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomMonthPickerDialog
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomYearPickerDialog
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Resource
import java.util.Calendar

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun OverViewAnalysisScreen(
    viewModel: OverViewAnalysisViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    bottomPadding: PaddingValues,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    //val currency by viewModel.currency.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val currentPeriod by remember { viewModel.currentPeriod }.collectAsStateWithLifecycle()
    val currentYearInMilli by remember { viewModel.currentYearInMilli }.collectAsStateWithLifecycle()
    val currentMonthInMilli by remember { viewModel.currentMonthInMilli }.collectAsStateWithLifecycle()
    val categoryExpense by remember { viewModel.categoryExpense }.collectAsStateWithLifecycle()
    val currentTotal by remember { viewModel.currentTotal }.collectAsStateWithLifecycle()
    var openYearDialog by remember { mutableStateOf(false) }
    var openMonthDialog by remember { mutableStateOf(false) }
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AnalysisTopSelectionButton(
                list = AnalysisPeriod.entries,
                selectedPeriod = currentPeriod,
                onSelect = viewModel::setCurrentPeriod,
            )

            val timeInMilli = when (currentPeriod) {
                AnalysisPeriod.MONTH -> currentMonthInMilli
                AnalysisPeriod.YEAR -> currentYearInMilli
            }

            AnalysisMonthYearSelection(
                textYearMonth = viewModel.formatDateForDisplay(currentPeriod, timeInMilli),
                onPreviousClick = viewModel::onPreviousClick,
                onNextClick = viewModel::onNextClick,
                onTextClick = {
                    if (isLoading)
                        return@AnalysisMonthYearSelection
                    when (currentPeriod) {
                        AnalysisPeriod.MONTH -> openMonthDialog = true
                        AnalysisPeriod.YEAR -> openYearDialog = true
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

    if (openMonthDialog) {
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
                viewModel.setCurrentMonth(year = year, month = month)
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
                viewModel.setCurrentYear(it)
            }
        )
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