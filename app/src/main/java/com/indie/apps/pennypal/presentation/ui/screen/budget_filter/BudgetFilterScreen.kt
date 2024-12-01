package com.indie.apps.pennypal.presentation.ui.screen.budget_filter

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
import com.indie.apps.pennypal.data.database.enum.BudgetMenu
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.screen.add_budget.AddBudgetTopSelectionButton
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BudgetFilterScreen(
    viewModel: BudgetFilterViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onAddClick: (Int) -> Unit,
    onBudgetEditClick: (Long) -> Unit,
    budgetFilterId :Int,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val currentPeriod by viewModel.currentPeriod.collectAsStateWithLifecycle()

    val title = if(budgetFilterId == BudgetMenu.PAST.id)
        stringResource(id = R.string.past_boudget)
    else
        stringResource(id = R.string.upcoming_boudget)

    Scaffold(
        topBar = {
            BudgetFilterTopBar(
                title = title,
                onNavigationUp = onNavigationUp,
                onAddClick = {onAddClick(currentPeriod)}
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

            AddBudgetTopSelectionButton(
                list = PeriodType.entries,
                selectBudgetPeriod = PeriodType.entries.first { it.id == currentPeriod },
                onSelect = viewModel::setCurrentPeriod,
            )
        }
    }

}

@Preview
@Composable
private fun OverViewScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        BudgetFilterScreen(
            onNavigationUp = {},
            onAddClick = {},
            onBudgetEditClick = {},
            budgetFilterId = 1
        )
    }
}