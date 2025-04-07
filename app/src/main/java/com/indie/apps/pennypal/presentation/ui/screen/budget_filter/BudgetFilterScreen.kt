package com.indie.apps.pennypal.presentation.ui.screen.budget_filter

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.enum.BudgetMenu
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomProgressItemWithDate
import com.indie.apps.pennypal.presentation.ui.screen.add_budget.AddBudgetTopSelectionButton
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getDateFromMillis
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@SuppressLint(
    "StateFlowValueCalledInComposition", "SimpleDateFormat",
    "CoroutineCreationDuringComposition"
)
@Composable
fun BudgetFilterScreen(
    viewModel: BudgetFilterViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onAddClick: (Int) -> Unit,
    onBudgetEditClick: (Long) -> Unit,
    budgetFilterId: Int,
    periodType: Int?,
    budgetId: Long?,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val lazyPagingData = viewModel.pagedData.collectAsLazyPagingItems()
    val pagingState by viewModel.pagingState.collectAsStateWithLifecycle()
    pagingState.update(lazyPagingData)
    val addBudgetAnimRun by viewModel.addBudgetAnimRun.collectAsStateWithLifecycle()
    // val currency by viewModel.currency.collectAsStateWithLifecycle()

    val currentPeriod by viewModel.currentPeriod.collectAsStateWithLifecycle()

    val title = if (budgetFilterId == BudgetMenu.PAST.id)
        stringResource(id = R.string.past_boudget)
    else
        stringResource(id = R.string.upcoming_boudget)

    LaunchedEffect(budgetFilterId) {
        viewModel.setFilterId(budgetFilterId)
    }

    LaunchedEffect(periodType) {
        if (periodType != null)
            viewModel.setCurrentPeriod(periodType)
    }
    var addBudgetId by remember {
        mutableLongStateOf(-1L)
    }

    LaunchedEffect(budgetId) {
        if (budgetId != null) {
            addBudgetId = budgetId
            viewModel.addBudgetSuccess()
        }

    }

    Scaffold(
        topBar = {
            BudgetFilterTopBar(
                title = title,
                onNavigationUp = onNavigationUp,
                onAddClick = { onAddClick(currentPeriod) }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(id = R.dimen.item_inner_padding)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            AddBudgetTopSelectionButton(
                list = PeriodType.entries,
                selectBudgetPeriod = PeriodType.entries.first { it.id == currentPeriod },
                onSelect = {
                    viewModel.setCurrentPeriod(it.id)
                },
            )

            if (pagingState.isRefresh && (lazyPagingData.itemCount == 0)) {
                LoadingWithProgress(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MyAppTheme.colors.transparent)
                )
            } else if (lazyPagingData.itemCount == 0) {
                NoDataMessage(
                    title = stringResource(id = R.string.no_budget_found),
                    details = "",
                    iconSize = 70.dp,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {

                if (!pagingState.isRefresh && lazyPagingData.itemCount != 0) {
                    val scope = rememberCoroutineScope()

                    val scrollState: LazyListState = rememberLazyListState(
                        0, 0
                    )

                    LaunchedEffect(lazyPagingData.itemCount) {
                        if (addBudgetAnimRun)
                            scrollState.scrollToItem(0, 0)
                    }

                    LazyColumn(
                        state = scrollState,
                    ) {
                        items(
                            count = lazyPagingData.itemCount,
                            key = lazyPagingData.itemKey { item -> item.id }
                        ) { index ->

                            val item = lazyPagingData[index]
                            if (item != null) {

                                val dateFormat = SimpleDateFormat("dd MMM yyyy")
                                val yearFormat = SimpleDateFormat("yyyy")
                                val monthFormat = SimpleDateFormat("MMMM yyyy")

                                val timeString = item.let { tmpBudgetData ->
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
                                }
                                val itemAnimateScale = remember {
                                    androidx.compose.animation.core.Animatable(0f)
                                }

                                val modifierAdd: Modifier =
                                    if (addBudgetId == item.id && addBudgetAnimRun) {
                                        scope.launch {
                                            itemAnimateScale.animateTo(
                                                targetValue = 1f,
                                                animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
                                            )
                                        }
                                        if (itemAnimateScale.value == 1f) {
                                            viewModel.addBudgetSuccessAnimStop()
                                        }
                                        Modifier.scale(itemAnimateScale.value)
                                    } else {
                                        Modifier
                                    }

                                CustomProgressItemWithDate(
                                    name = item.title,
                                    totalAmount = item.budgetAmount,
                                    spentAmount = item.spentAmount,
                                    date = timeString,
                                    onClick = {
                                        onBudgetEditClick(item.id)
                                    },
                                    modifier = modifierAdd,
                                    currency = item.originalAmountSymbol
                                )
                            }
                        }
                    }
                }
            }
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
            budgetFilterId = 1,
            periodType = null,
            budgetId = null
        )
    }
}