package com.indie.apps.pennypal.presentation.ui.screen.budget

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BudgetScreen(
    budgetViewModel: BudgetViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onAddClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val title = stringResource(id = R.string.budget)

    val monthBudgetState by budgetViewModel.monthlyBudgets.collectAsStateWithLifecycle()
    val yearBudgetState by budgetViewModel.yearlyBudgets.collectAsStateWithLifecycle()
    val oneTimeBudgetState by budgetViewModel.oneTimeBudgets.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            BudgetTopBar(
                title = title,
                onNavigationUp = onNavigationUp,
                onAddClick = onAddClick
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

            BudgetGroupItem(
                title = R.string.month,
                budgetList = monthBudgetState
            )

            BudgetGroupItem(
                title = R.string.year,
                budgetList = yearBudgetState
            )

            BudgetGroupItem(
                title = R.string.one_time,
                budgetList = oneTimeBudgetState
            )
        }
    }

}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        BudgetScreen(
            onNavigationUp = {},
            onAddClick = {}
        )
    }
}