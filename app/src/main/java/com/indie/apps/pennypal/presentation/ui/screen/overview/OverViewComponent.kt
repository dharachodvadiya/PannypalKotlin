package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.User
import com.indie.apps.pennypal.data.database.enum.PeriodType
import com.indie.apps.pennypal.data.module.ChartData
import com.indie.apps.pennypal.data.module.MerchantDataWithAllData
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.data.module.balance.TotalWithCurrency
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.presentation.ui.component.PeriodText
import com.indie.apps.pennypal.presentation.ui.component.chart.PieChart
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.AutoSizeText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomTab
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImageWithText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.screen.all_data.TransactionItem
import com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis.SingleBudgetOverAllAnalysis
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getCategoryColor
import com.indie.apps.pennypal.util.getColorFromId
import com.indie.apps.pennypal.util.getDateFromMillis
import com.indie.apps.pennypal.util.getFirstCharacterUppercase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.enums.EnumEntries

@Composable
fun OverviewTopBar(
    //onSearchTextChange: (String) -> Unit,
    onProfileClick: () -> Unit, modifier: Modifier = Modifier
) {/*var isSearch by remember { mutableStateOf(false) }

    TopBar(
        isBackEnable = isSearch,
        onBackClick = { isSearch = false },
        leadingContent = { if (!isSearch) OverviewTopBarProfile(onClick = onProfileClick) },
        content = {
            if (isSearch)
                SearchView(
                    onTextChange = onSearchTextChange
                )
        },
        trailingContent = {
            if (!isSearch) {
                IconButton(onClick = {
                    isSearch = true
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Profile",
                        tint = MyAppTheme.colors.black
                    )
                }
            }
        },
        modifier = modifier
    )*/

    TopBar(
        isBackEnable = false,
        leadingContent = {
            OverviewTopBarProfile(
                onClick = onProfileClick,
                user = null,
                isSubscribed = false,
                onSubscriptionChanged = {})
        },
        modifier = modifier
    )
}

/*@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun OverviewList(
    dataWithDayList: LazyPagingItems<MerchantDataWithNameWithDayTotal>,
    modifier: Modifier = Modifier,
    isLoadMore: Boolean = false,
    isAddMerchantDataSuccess: Boolean = false,
    merchantDataId: Long = 1L,
    onAnimStop: () -> Unit,
    bottomPadding: PaddingValues
) {
    val scope = rememberCoroutineScope()
    if (dataWithDayList.itemCount == 0) {
        NoDataMessage(
            title = stringResource(id = R.string.no_transactions_yet),
            details = stringResource(id = R.string.latest_transactions_appear_here),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottomPadding)
        )
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding)),
            //verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_padding)),
            contentPadding = bottomPadding
        ) {

            *//*var totalListIndex = 0
            items(
                count = dataList.itemCount,
                key = dataList.itemKey { item -> item.id },
                contentType = dataList.itemContentType { "MerchantDataWithName" }
            ) { index ->
                val data = dataList[index]
                if (data != null) {
                    var isDateShow = false

                    if (totalListIndex < dailyTotalList.itemCount) {
                        if (index == 0 || (data.day == dailyTotalList[totalListIndex]?.day &&
                                    (dataList[index - 1]?.day
                                        ?: "") != dailyTotalList[totalListIndex]?.day)
                        )
                            isDateShow = true
                    }

                    val itemAnimateScale = remember {
                        Animatable(0f)
                    }

                    val modifierAdd: Modifier =
                        if (merchantDataId == data.id && isAddMerchantDataSuccess) {
                            scope.launch {
                                itemAnimateScale.animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(50)
                                )
                            }
                            if (itemAnimateScale.value == 1f) {
                                onAnimStop()
                            }
                            Modifier.scale(itemAnimateScale.value)
                        } else {
                            Modifier
                        }

                    Column() {
                        if (isDateShow) {
                            if (index != 0)
                                Spacer(modifier = Modifier.height(13.dp))
                            dailyTotalList[totalListIndex]?.let { OverviewListDateItem(it) }
                            totalListIndex++
                        }
                        OverviewListItem(
                            item = data,
                            modifier = modifierAdd
                        )
                    }


                    if (isLoadMore && index == dataList.itemCount) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }*//*

            items(count = dataWithDayList.itemCount,
                contentType = dataWithDayList.itemContentType { "Any" }) { index ->
                val data = dataWithDayList[index]
                if (data != null) {

                    val itemAnimateScale = remember {
                        Animatable(0f)
                    }

                    val modifierAdd: Modifier =
                        if (merchantDataId == data.id && isAddMerchantDataSuccess) {
                            scope.launch {
                                itemAnimateScale.animateTo(
                                    targetValue = 1f, animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
                                )
                            }
                            if (itemAnimateScale.value == 1f) {
                                onAnimStop()
                            }
                            Modifier.scale(itemAnimateScale.value)
                        } else {
                            Modifier
                        }

                    if (data.id == null) {
                        if (index != 0) Spacer(modifier = Modifier.height(13.dp))
                        OverviewListDateItem(data.toMerchantDataDailyTotal())
                    } else {
                        OverviewListItem(
                            item = data.toMerchantDataWithName(), modifier = modifierAdd
                        )
                    }*//*when (data) {
                        is MerchantDataDailyTotal -> OverviewListDateItem(data)

                        is MerchantDataWithName -> {

                            val itemAnimateScale = remember {
                                Animatable(0f)
                            }

                            val modifierAdd: Modifier =
                                if (merchantDataId == data.id && isAddMerchantDataSuccess) {
                                    scope.launch {
                                        itemAnimateScale.animateTo(
                                            targetValue = 1f,
                                            animationSpec = tween(50)
                                        )
                                    }
                                    if (itemAnimateScale.value == 1f) {
                                        onAnimStop()
                                    }
                                    Modifier.scale(itemAnimateScale.value)
                                } else {
                                    Modifier
                                }

                            OverviewListItem(
                                item = data,
                                modifier = modifierAdd
                            )
                        }
                    }*//*

                    if (isLoadMore && index == dataWithDayList.itemCount) {
                        Box(
                            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }


            }


        }
    }
}

@Composable
fun OverviewListDateItem(
    item: DailyTotal,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val dayString = when (item.day) {
        Util.getTodayDate() -> stringResource(id = R.string.today)
        Util.getYesterdayDate() -> stringResource(id = R.string.yesterday)
        else -> item.day
    }
    val amount = item.totalIncome - item.totalExpense
    val totalTextColor = if (amount >= 0) MyAppTheme.colors.greenText else MyAppTheme.colors.redText
    Column(modifier = modifier) {
        CustomText(
            text = dayString,
            style = MyAppTheme.typography.Semibold40,
            color = MyAppTheme.colors.gray1
        )
        AutoSizeText(
            text = Util.getFormattedStringWithSymbol(amount),
            style = MyAppTheme.typography.Regular46,
            color = totalTextColor,
            maxLines = 1,
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }

}*/

@Composable
fun OverviewBalanceItem(
    amount: String,
    @StringRes title: Int,
    imageVector: ImageVector,
    horizontalAlignment: Alignment.Horizontal,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = horizontalAlignment
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            RoundImage(
                imageVector = imageVector,
                tint = MyAppTheme.colors.gray1,
                backGround = MyAppTheme.colors.brandBg.copy(alpha = 0.3f),
                contentDescription = "amount",
                imageVectorSize = 13.dp,
                modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            CustomText(
                text = stringResource(title),
                style = MyAppTheme.typography.Regular46,
                color = MyAppTheme.colors.gray1
            )
        }
        AutoSizeText(
            text = amount,
            style = MyAppTheme.typography.Regular51,
            color = MyAppTheme.colors.black,
            maxLines = 1,
        )
    }
}

/*@Composable
fun OverviewListItem(
    item: MerchantDataWithName,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = if (item.type < 0) Icons.Default.NorthEast else Icons.Default.SouthWest
    val bgColor = if (item.type < 0) MyAppTheme.colors.redBg else MyAppTheme.colors.greenBg

    ListItem(isClickable = false, leadingIcon = {
        RoundImage(
            imageVector = imageVector,
            tint = MyAppTheme.colors.black,
            backGround = bgColor,
            contentDescription = "amount"
        )
    }, content = {
        Column {
            CustomText(
                text = item.merchantName,
                style = MyAppTheme.typography.Semibold52_5,
                color = MyAppTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!item.details.isNullOrEmpty()) {
                CustomText(
                    text = item.details,
                    style = MyAppTheme.typography.Medium33,
                    color = MyAppTheme.colors.gray1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }, trailingContent = {
        CustomText(
            text = Util.getFormattedStringWithSymbol(if (item.type > 0) item.amount else item.amount * -1),
            style = MyAppTheme.typography.Regular51,
            color = MyAppTheme.colors.black,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(0.5f),
            textAlign = TextAlign.Right,
            maxLines = 1
        )
    }, isSetDivider = false, modifier = modifier.padding(vertical = 5.dp)
    )
}*/

@Composable
fun OverviewData(
    currentPeriod: String,
    data: TotalWithCurrency?,
    symbol: String,
    recentTransaction: List<MerchantDataWithAllData>,
    categoryList: List<CategoryAmount>,
    recentMerchant: List<MerchantNameAndDetails>,
    budgetWithSpentAndCategoryIdList: BudgetWithSpentAndCategoryIdList?,
    selectBudgetPeriod: PeriodType,
    onSelectBudgetPeriod: (PeriodType) -> Unit,
    onSeeAllTransactionClick: () -> Unit,
    onSeeAllMerchantClick: () -> Unit,
    onExploreAnalysisClick: () -> Unit,
    onExploreBudgetClick: () -> Unit,
    onTransactionClick: (Long) -> Unit,
    isAddMerchantDataSuccess: Boolean = false,
    isEditMerchantDataSuccess: Boolean = false,
    merchantDataId: Long = 1L,
    onAnimStop: () -> Unit
) {

    PeriodText(
        text = currentPeriod,
        textStyle = MyAppTheme.typography.Regular51,
    )

    OverviewBalanceView(
        data = data,
        symbol = symbol,
    )

    OverviewTransactionData(
        onTransactionClick = onTransactionClick,
        recentTransaction = recentTransaction,
        onSeeAllTransactionClick = onSeeAllTransactionClick,
        isAddMerchantDataSuccess = isAddMerchantDataSuccess,
        isEditMerchantDataSuccess = isEditMerchantDataSuccess,
        merchantDataId = merchantDataId,
        onAnimStop = onAnimStop
    )

    OverviewMerchantData(
        recentMerchant = recentMerchant,
        onSeeAllMerchantClick = onSeeAllMerchantClick
    )

    OverviewAnalysisData(
        currentPeriod = currentPeriod,
        categoryList = categoryList,
        onExploreAnalysisClick = onExploreAnalysisClick
    )

    OverviewBudgetData(
        budgetWithSpentAndCategoryIdList = budgetWithSpentAndCategoryIdList,
        onExploreBudgetClick = onExploreBudgetClick,
        selectBudgetPeriod = selectBudgetPeriod,
        onSelectBudgetPeriod = onSelectBudgetPeriod
    )
}

@Composable
fun OverviewBalanceView(
    data: TotalWithCurrency?,
    symbol: String,
    modifier: Modifier = Modifier,
) {

    val balance = if (data == null) {
        0.0
    } else {
        data.totalIncome - data.totalExpense
    }
    val colorStroke =
        if (balance >= 0) MyAppTheme.colors.greenBg else MyAppTheme.colors.redBg

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
        //.background(brush = verticalGradientsBrush(colorGradient))
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
            color = MyAppTheme.colors.lightBlue2,
            shadowElevation = dimensionResource(id = R.dimen.shadow_elevation)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawLine(
                            colorStroke, Offset(0f, 0f), Offset(size.width, 0f), 25f
                        )
                    }, contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding)),
                ) {
                    Column {
                        CustomText(
                            text = stringResource(id = R.string.balance),
                            style = MyAppTheme.typography.Regular57,
                            color = MyAppTheme.colors.gray1
                        )
                        AutoSizeText(
                            text = Util.getFormattedStringWithSymbol(balance, symbol),
                            style = MyAppTheme.typography.Regular66_5,
                            color = MyAppTheme.colors.black,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        OverviewBalanceItem(
                            amount = Util.getFormattedStringWithSymbol(data?.totalIncome ?: 0.0),
                            title = R.string.received,
                            horizontalAlignment = Alignment.Start,
                            imageVector = Icons.Default.SouthWest,
                        )
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = dimensionResource(id = R.dimen.item_inner_padding))
                        )
                        OverviewBalanceItem(
                            amount = Util.getFormattedStringWithSymbol(data?.totalExpense ?: 0.0),
                            title = R.string.spent,
                            horizontalAlignment = Alignment.End,
                            imageVector = Icons.Default.NorthEast,
                        )
                    }

                }
            }

        }
    }


}

@Composable
fun OverviewTransactionData(
    recentTransaction: List<MerchantDataWithAllData>,
    onSeeAllTransactionClick: () -> Unit,
    onTransactionClick: (Long) -> Unit,
    isAddMerchantDataSuccess: Boolean = false,
    isEditMerchantDataSuccess: Boolean = false,
    merchantDataId: Long = 1L,
    onAnimStop: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OverviewItem(
        title = stringResource(id = R.string.recent_transaction),
        onSeeAllClick = onSeeAllTransactionClick,
        content = {
            val scope = rememberCoroutineScope()

            recentTransaction.forEach { item ->

                val itemAnimateScale = remember {
                    Animatable(0f)
                }

                val baseColor = MyAppTheme.colors.itemBg
                val targetAnimColor = MyAppTheme.colors.lightBlue1

                val itemAnimateColor = remember {
                    androidx.compose.animation.Animatable(baseColor)
                }

                val modifierAdd: Modifier =
                    if (merchantDataId == item.id && isAddMerchantDataSuccess) {
                        scope.launch {
                            itemAnimateScale.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
                            )
                        }
                        if (itemAnimateScale.value == 1f) {
                            onAnimStop()
                        }
                        Modifier.scale(itemAnimateScale.value)
                    } else if ((merchantDataId == item.id && isEditMerchantDataSuccess)
                    ) {
                        scope.launch {
                            itemAnimateColor.animateTo(
                                targetValue = targetAnimColor,
                                animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                            )
                            itemAnimateColor.animateTo(
                                targetValue = baseColor,
                                animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                            )
                        }
                        Modifier
                    } else {
                        Modifier
                    }

                TransactionItem(
                    item = item,
                    isSelected = false,
                    onClick = {
                        onTransactionClick(item.id)
                    },
                    onLongClick = { },
                    itemBgColor = itemAnimateColor.value,
                    modifier = modifierAdd
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun OverviewMerchantData(
    recentMerchant: List<MerchantNameAndDetails>,
    onSeeAllMerchantClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OverviewItem(
        title = stringResource(id = R.string.recent_merchant),
        onSeeAllClick = onSeeAllMerchantClick,
        content = {
            Row(
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                recentMerchant.forEach { item ->
                    MerchantItem(
                        item = item,
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                repeat(4 - recentMerchant.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

        },
        modifier = modifier
    )
}

@Composable
fun OverviewAnalysisData(
    currentPeriod: String,
    onExploreAnalysisClick: () -> Unit,
    categoryList: List<CategoryAmount>,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OverviewItem(
        title = stringResource(id = R.string.analysis_of) + " $currentPeriod",
        trailingText = R.string.explore,
        onSeeAllClick = onExploreAnalysisClick,
        content = {
            val chartData = categoryList.map { item ->
                ChartData(
                    name = item.name,
                    amount = item.amount,
                    color = getCategoryColor(item.name)
                )
            }

            Row(
                modifier = Modifier
                    .clickable { onExploreAnalysisClick() }
            ) {
                PieChart(
                    data = chartData,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_inner_padding)))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 20.dp)
                ) {
                    if (chartData.isEmpty()) {
                        OverviewAnalyticDataItem(
                            stringResource(id = R.string.no_data),
                            MyAppTheme.colors.gray3.copy(alpha = 0.5f)
                        )
                    } else {
                        chartData.forEach { item ->
                            OverviewAnalyticDataItem(item.name, item.color)
                        }
                    }
                }
            }
        },
        isBgEnable = true,
        modifier = modifier
    )
}

@SuppressLint("SimpleDateFormat")
@Composable
fun OverviewBudgetData(
    budgetWithSpentAndCategoryIdList: BudgetWithSpentAndCategoryIdList?,
    onExploreBudgetClick: () -> Unit,
    selectBudgetPeriod: PeriodType,
    onSelectBudgetPeriod: (PeriodType) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OverviewItem(
        title = stringResource(id = R.string.your_budget),
        trailingText = R.string.explore,
        onSeeAllClick = onExploreBudgetClick,
        content = {
            if (budgetWithSpentAndCategoryIdList != null) {
                val yearFormat = SimpleDateFormat("yyyy")
                val monthFormat = SimpleDateFormat("MMMM yyyy")

                val timeString = budgetWithSpentAndCategoryIdList.let { tmpBudgetData ->
                    when (budgetWithSpentAndCategoryIdList.periodType) {
                        PeriodType.MONTH.id -> getDateFromMillis(
                            tmpBudgetData.startDate,
                            monthFormat
                        )

                        PeriodType.YEAR.id -> getDateFromMillis(
                            tmpBudgetData.startDate,
                            yearFormat
                        )

                        else -> ""
                    }
                } ?: ""

                OverviewBudgetSelectionButton(
                    list = PeriodType.entries,
                    selectBudgetPeriod = PeriodType.entries.first { it == selectBudgetPeriod },
                    onSelect = onSelectBudgetPeriod
                )

                SingleBudgetOverAllAnalysis(
                    totalBudgetAmount = budgetWithSpentAndCategoryIdList.budgetAmount,
                    spentAmount = budgetWithSpentAndCategoryIdList.spentAmount,
                    timeString = timeString
                )

            }

        },
        isBgEnable = true,
        modifier = modifier.clickable { onExploreBudgetClick() }
    )
}

@Composable
fun OverviewAnalyticDataItem(
    name: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color = color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        CustomText(
            text = name,
            color = MyAppTheme.colors.gray0,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}

@Composable
fun MerchantItem(
    item: MerchantNameAndDetails,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(5.dp)
    ) {
        /*RoundImage(
            imageVector = Icons.Default.Person,
            imageVectorSize = 30.dp,
            //brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
            tint = MyAppTheme.colors.black,
            backGround = MyAppTheme.colors.lightBlue2,
            contentDescription = "person",
            modifier = Modifier.size(55.dp)
        )*/

        RoundImageWithText(
            character = getFirstCharacterUppercase(item.name),
            tint = MyAppTheme.colors.white,
            backGround = getColorFromId(item.id.toInt()),
            modifier = Modifier.size(55.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))

        CustomText(
            text = item.name,
            style = MyAppTheme.typography.Regular44,
            color = MyAppTheme.colors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (!item.details.isNullOrEmpty()) {
            CustomText(
                text = item.details,
                style = MyAppTheme.typography.Medium34,
                color = MyAppTheme.colors.gray2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun OverviewItem(
    onSeeAllClick: () -> Unit,
    title: String,
    @StringRes trailingText: Int = R.string.see_all,
    content: @Composable (() -> Unit),
    enableSeeAll: Boolean = true,
    isBgEnable: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomText(
                text = title,
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.gray1
            )

            Spacer(modifier = Modifier.weight(1f))

            if (enableSeeAll) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onSeeAllClick() }
                ) {
                    CustomText(
                        text = stringResource(id = trailingText),
                        style = MyAppTheme.typography.Semibold40,
                        color = MyAppTheme.colors.lightBlue1
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "all",
                        tint = MyAppTheme.colors.lightBlue1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.item_inner_padding)))

        if (isBgEnable) {
            Column(
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.itemBg)
            )
            {
                content()
            }
        } else {
            content()
        }

    }

}

@Composable
fun OverviewTopBarProfile(
    user: User?,
    isSubscribed: Boolean,
    onClick: () -> Unit,
    onSubscriptionChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {/*Surface(
        onClick = onClick,
        modifier = modifier
            .semantics { role = Role.Button },
        shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
        contentColor = MyAppTheme.colors.gray1
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(dimensionResource(R.dimen.top_bar_profile))
                .roundedCornerBackground(MyAppTheme.colors.white, BorderStroke(width = 1.dp, MyAppTheme.colors.gray1))
                *//*.border(
                    border = BorderStroke(
                        width = 1.dp,
                        MyAppTheme.colors.gray1
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                )
                .background(MyAppTheme.colors.white)*//*
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Profile"
            )
        }

    }*/


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        PrimaryButton(
            bgColor = MyAppTheme.colors.white,
            borderStroke = BorderStroke(
                width = 1.dp,
                color = MyAppTheme.colors.gray1
            ),
            onClick = onClick,
            modifier = Modifier.size(dimensionResource(R.dimen.top_bar_profile))
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile",
                tint = MyAppTheme.colors.gray1
            )
        }

        Spacer(modifier = Modifier.width(15.dp))

        CustomText(
            text = user?.name ?: "",
            style = MyAppTheme.typography.Regular57,
            color = MyAppTheme.colors.black
        )

        Spacer(modifier = Modifier.weight(1f))


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isSubscribed, onCheckedChange = onSubscriptionChanged)
            CustomText(
                text = "Subscribed",
                style = MyAppTheme.typography.Regular57,
                color = MyAppTheme.colors.black
            )
        }

    }
}
/*
@Composable
fun OverviewAppFloatingButton(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    PrimaryButton(
        //bgBrush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
        bgColor = MyAppTheme.colors.buttonBg, onClick = onClick, modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Entry",
                tint = MyAppTheme.colors.gray1
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.new_entry),
                style = MyAppTheme.typography.Medium45_29,
                color = MyAppTheme.colors.gray1
            )
        }
    }
}*/

@Composable
fun OverviewBudgetSelectionButton(
    list: EnumEntries<PeriodType>,
    selectBudgetPeriod: PeriodType,
    onSelect: (PeriodType) -> Unit,
    modifier: Modifier = Modifier,
) {

    val tabItems = list.filter { it == PeriodType.MONTH || it == PeriodType.YEAR }.map { period ->
        TabItemInfo(
            title = when (period) {
                PeriodType.MONTH -> R.string.month
                PeriodType.YEAR -> R.string.year
                PeriodType.ONE_TIME -> R.string.one_time
            },
            selectBgColor = MyAppTheme.colors.itemSelectedBg,
            unSelectBgColor = MyAppTheme.colors.bottomBg,
            selectContentColor = MyAppTheme.colors.black,
            unSelectContentColor = MyAppTheme.colors.gray1
        )
    }

    Row(
        modifier = modifier
            .padding(
                top = dimensionResource(id = R.dimen.item_padding),
                end = dimensionResource(id = R.dimen.item_padding)
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        CustomTab(
            tabList = tabItems,
            selectedIndex = list.indexOf(selectBudgetPeriod),
            onTabSelected = {
                onSelect(list[it])
            },
            bgColor = MyAppTheme.colors.bottomBg,
            paddingValues = PaddingValues(2.dp),
            modifier = Modifier.width(120.dp),
            tabVerticalPadding = 3.dp
        )

    }

}

@Preview(showBackground = true)
@Composable
private fun TopBarProfilePreview() {
    PennyPalTheme(darkTheme = true) {
        OverviewTopBarProfile(
            onClick = { },
            user = null,
            isSubscribed = false,
            onSubscriptionChanged = {})
    }
}

@Preview
@Composable
private fun OverviewListItemPreview() {
    PennyPalTheme(darkTheme = true) {
        // OverviewListItem()
    }
}