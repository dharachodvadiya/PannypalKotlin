package com.indie.apps.pennypal.presentation.ui.screen.budget

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.util.enum.DialogType
import com.indie.apps.pennypal.util.enum.PeriodType
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomProgressItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomProgressItemWithDate
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerLowerBackground
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerUpperBackground
import com.indie.apps.pennypal.presentation.ui.screen.overview_analysis.AnalysisMonthYearSelection
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getDateFromMillis
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun BudgetTopBar(
    title: String = "",
    onNavigationUp: () -> Unit,
    onAddClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    //var expanded by remember { mutableStateOf(false) }

    TopBarWithTitle(
        title = title, onNavigationUp = {
            onNavigationUp()
        },
        trailingContent = {

            PrimaryButton(
                bgColor = MyAppTheme.colors.white,
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = MyAppTheme.colors.gray1
                ),
                onClick = onAddClick,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.top_bar_profile))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MyAppTheme.colors.gray1
                )
            }

            /* Column {
                 IconButton(onClick = { expanded = !expanded }) {
                     Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                 }

                 DropdownMenu(
                     expanded = expanded,
                     onDismissRequest = { expanded = false }
                 ) {
                     DropdownMenuItem(
                         onClick = {
                             expanded = false
                             onBudgetMenuClick(BudgetMenu.UPCOMING.id)
                         },
                         text = { CustomText(text = stringResource(id = R.string.upcoming_boudget)) })

                     DropdownMenuItem(
                         onClick = {
                             expanded = false
                             onBudgetMenuClick(BudgetMenu.PAST.id)
                         },
                         text = { CustomText(text = stringResource(id = R.string.past_boudget)) })
                 }
             }*/
        },
        contentAlignment = Alignment.Center,
        modifier = modifier
    )
}

@Composable
fun BudgetGroupItem(
    title: Int,
    budgetList: List<BudgetWithSpentAndCategoryIdList>,
    currency: String,
    onBudgetItemClick: (Long) -> Unit,
    noDataTitleId: Int,
    noDataDetailId: Int,
    btnTextId: Int,
    onAddClick: () -> Unit,
    isShowAddButton: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            .padding(dimensionResource(id = R.dimen.item_inner_padding))
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomText(
                text = stringResource(id = title),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.gray1
            )

            if (isShowAddButton && budgetList.isNotEmpty()) {

                Spacer(modifier = Modifier.weight(1f))
                PrimaryButton(
                    onClick = onAddClick
                ) {
                    CustomText(
                        text = stringResource(id = R.string.set_up_budget),
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray0,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        budgetList.forEach { item ->
            CustomProgressItem(
                name = item.title,
                totalAmount = item.budgetAmount,
                spentAmount = item.spentAmount,
                currency = currency,
                onClick = {
                    onBudgetItemClick(item.id)
                }
            )
        }

        if (budgetList.isEmpty()) {

            NoDataMessage(
                title = stringResource(id = noDataTitleId),
                details = stringResource(id = noDataDetailId),
                iconSize = 0.dp,
                titleColor = MyAppTheme.colors.gray0,
                detailsColor = MyAppTheme.colors.gray2,
                titleTextStyle = MyAppTheme.typography.Medium54,
                isClickable = false,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.item_inner_padding))
            )

            PrimaryButton(onClick = onAddClick) {
                CustomText(
                    text = stringResource(id = btnTextId),
                    style = MyAppTheme.typography.Regular44,
                    color = MyAppTheme.colors.gray0,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}

@Composable
fun BudgetExpandableGroupHeader(
    title: Int,
    isExpand: Boolean = false,
    onExpandClick: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            //.roundedCornerBackground(MyAppTheme.colors.itemBg)
            .padding(dimensionResource(id = R.dimen.item_inner_padding))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickableWithNoRipple(
            ) { onExpandClick() }
        ) {
            CustomText(
                text = stringResource(id = title),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.gray1
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = if (isExpand) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                tint = MyAppTheme.colors.gray1
            )
        }
        if (isExpand) {
            Spacer(Modifier.height(5.dp))
            HorizontalDivider(color = MyAppTheme.colors.gray3.copy(alpha = 0.5f))
        }
    }
}


@Composable
fun BudgetExpandableGroupItem(
    title: Int,
    lazyPagingData: LazyPagingItems<BudgetWithSpentAndCategoryIdList>,
    currency: String,
    onBudgetItemClick: (Long) -> Unit,
    onExpandClick: () -> Unit,
    isExpand: Boolean,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            .padding(dimensionResource(id = R.dimen.item_inner_padding))
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickableWithNoRipple { onExpandClick() }
        ) {
            CustomText(
                text = stringResource(id = title),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.gray1
            )
            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = if (isExpand) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Add",
                tint = MyAppTheme.colors.gray1,

                )
        }

        if (isExpand) {

            Spacer(Modifier.height(5.dp))
            HorizontalDivider(color = MyAppTheme.colors.gray3.copy(alpha = 0.5f))

            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_padding))
            ) {
                items(
                    count = lazyPagingData.itemCount,
                    key = lazyPagingData.itemKey { item -> item.id }
                ) { index ->

                    val item = lazyPagingData[index]
                    if (item != null) {

                        val dateFormat = SimpleDateFormat("dd MMM yyyy")

                        val timeString = item.let { tmpBudgetData ->
                            "${
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
                        }

                        CustomProgressItemWithDate(
                            name = item.title,
                            totalAmount = item.budgetAmount,
                            spentAmount = item.spentAmount,
                            date = timeString,
                            currency = currency,
                            onClick = {
                                onBudgetItemClick(item.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Helper Composable for Month/Year Selection
@Composable
fun MonthYearSelection(
    currentPeriod: PeriodType,
    currentMonthInMilli: Long,
    currentYearInMilli: Long,
    budgetViewModel: BudgetViewModel,
    onOpenDialog: (DialogType) -> Unit
) {
    val timeInMilli = when (currentPeriod) {
        PeriodType.MONTH -> currentMonthInMilli
        PeriodType.YEAR -> currentYearInMilli
        else -> 0
    }
    AnalysisMonthYearSelection(
        textYearMonth = budgetViewModel.formatDateForDisplay(currentPeriod, timeInMilli),
        onPreviousClick = budgetViewModel::onPreviousClick,
        onNextClick = budgetViewModel::onNextClick,
        onTextClick = {
            onOpenDialog(if (currentPeriod == PeriodType.MONTH) DialogType.Month else DialogType.Year)
        }
    )
}

// Helper Composable for Budget List (MONTH/YEAR)
@Composable
fun BudgetList(
    budgetState: List<BudgetWithSpentAndCategoryIdList>,
    //currency: String,
    addAnimRun: Boolean = false,
    deleteAnimRun: Boolean = false,
    budgetAnimId: Long = -1L,
    addDataSuccessAnimStop: () -> Unit,
    onBudgetEditClick: (Long) -> Unit
) {
    if (budgetState.isEmpty()) {
        NoDataMessage(
            title = stringResource(id = R.string.no_budget_found),
            details = "",
            iconSize = 70.dp,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
    } else {
        val scope = rememberCoroutineScope()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            budgetState.forEach { item ->

                val itemAnimateScale = remember {
                    Animatable(0f)
                }

                val modifierAdd: Modifier =
                    if (budgetAnimId == item.id && addAnimRun) {
                        scope.launch {
                            itemAnimateScale.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
                            )
                        }
                        if (itemAnimateScale.value == 1f) {
                            addDataSuccessAnimStop()
                        }
                        Modifier.scale(itemAnimateScale.value)
                    } else Modifier


                var visible by remember {
                    mutableStateOf(true)
                }

                if (deleteAnimRun && budgetAnimId == item.id) {
                    visible = false
                }

                AnimatedVisibility(
                    visible = visible,
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    CustomProgressItem(
                        name = item.title,
                        totalAmount = item.budgetAmount,
                        spentAmount = item.spentAmount,
                        onClick = { onBudgetEditClick(item.id) },
                        currency = item.originalAmountSymbol,
                        modifier = modifierAdd.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// Helper for Expandable Budget Group
fun LazyListScope.expandableBudgetGroup(
    title: Int,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    items: List<BudgetWithSpentAndCategoryIdList>? = null,
    pagingItems: LazyPagingItems<BudgetWithSpentAndCategoryIdList>? = null,
    //currency: String,
    addAnimRun: Boolean = false,
    deleteAnimRun: Boolean = false,
    budgetAnimId: Long = -1L,
    addDataSuccessAnimStop: () -> Unit,
    onBudgetEditClick: (Long) -> Unit
) {
    item {
        BudgetExpandableGroupHeader(
            title = title,
            isExpand = isExpanded,
            onExpandClick = onExpandClick,
            modifier = if (isExpanded) Modifier.roundedCornerUpperBackground(MyAppTheme.colors.itemBg)
            else Modifier.roundedCornerBackground(MyAppTheme.colors.itemBg)
        )
    }
    if (isExpanded) {
        when {
            items != null -> {
                items(
                    items = items,
                    key = { it.id }
                ) { item ->
                    /* BudgetItemContent(
                         item,
                         onBudgetEditClick,
                         items.indexOf(item) == items.size - 1
                     )*/

                    AnimItem(
                        addAnimRun = addAnimRun,
                        deleteAnimRun = deleteAnimRun,
                        budgetAnimId = budgetAnimId,
                        addDataSuccessAnimStop = addDataSuccessAnimStop,
                        item = item,
                        onBudgetEditClick = onBudgetEditClick,
                        isLastItem = items.indexOf(item) == items.size - 1
                    )
                }
            }

            pagingItems != null -> {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id }
                ) { index ->
                    pagingItems[index]?.let { item ->
                        /*  BudgetItemContent(
                              item,
                              onBudgetEditClick,
                              index == pagingItems.itemCount - 1
                          )*/

                        AnimItem(
                            addAnimRun = addAnimRun,
                            deleteAnimRun = deleteAnimRun,
                            budgetAnimId = budgetAnimId,
                            addDataSuccessAnimStop = addDataSuccessAnimStop,
                            item = item,
                            onBudgetEditClick = onBudgetEditClick,
                            isLastItem = index == pagingItems.itemCount - 1
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun AnimItem(
    item: BudgetWithSpentAndCategoryIdList,
    addAnimRun: Boolean = false,
    deleteAnimRun: Boolean = false,
    budgetAnimId: Long = -1L,
    addDataSuccessAnimStop: () -> Unit,
    onBudgetEditClick: (Long) -> Unit,
    isLastItem: Boolean
) {

    val scope = rememberCoroutineScope()

    val itemAnimateScale = remember {
        Animatable(0f)
    }

    val modifierAdd: Modifier =
        if (budgetAnimId == item.id && addAnimRun) {
            scope.launch {
                itemAnimateScale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
                )
            }
            if (itemAnimateScale.value == 1f) {
                addDataSuccessAnimStop()
            }
            Modifier.graphicsLayer {
                scaleX = 1f
                scaleY = itemAnimateScale.value
                transformOrigin = TransformOrigin(0.5f, 0f) // Center horizontally, top vertically
            }
        } else Modifier


    var visible by remember {
        mutableStateOf(true)
    }

    if (deleteAnimRun && budgetAnimId == item.id) {
        visible = false
    }

    AnimatedVisibility(
        visible = visible,
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        BudgetItemContent(
            item,
            onBudgetEditClick,
            isLastItem,
            modifierAdd
        )
    }
}

// Helper for Budget Item Content
@SuppressLint("SimpleDateFormat")
@Composable
fun BudgetItemContent(
    item: BudgetWithSpentAndCategoryIdList,
    onBudgetEditClick: (Long) -> Unit,
    isLastItem: Boolean,
    modifier: Modifier,
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy")
    val timeString = item.let { tmpBudgetData ->
        "${getDateFromMillis(tmpBudgetData.startDate, dateFormat)} - " +
                tmpBudgetData.endDate?.let { date -> getDateFromMillis(date, dateFormat) }
    }
    CustomProgressItemWithDate(
        name = item.title,
        totalAmount = item.budgetAmount,
        spentAmount = item.spentAmount,
        date = timeString,
        onClick = { onBudgetEditClick(item.id) },
        currency = item.originalAmountSymbol,
        modifier = if (isLastItem) modifier.roundedCornerLowerBackground(MyAppTheme.colors.itemBg)
        else modifier.background(MyAppTheme.colors.itemBg)
    )
}