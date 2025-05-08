package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.db_entity.User
import com.indie.apps.pennypal.data.module.ChartData
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.data.module.balance.Total
import com.indie.apps.pennypal.data.module.budget.BudgetWithSpentAndCategoryIdList
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithAllData
import com.indie.apps.pennypal.presentation.ui.component.composable.chart.PieChart
import com.indie.apps.pennypal.presentation.ui.component.composable.common.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.composable.common.UserProfileRect
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.AutoSizeText
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomTab
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.RoundImageWithText
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.addAnim
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.editAnim
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.removeAnim
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.screen.all_data.TransactionItem
import com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis.SingleBudgetOverAllAnalysis
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.AnimationType
import com.indie.apps.pennypal.util.app_enum.PeriodType
import com.indie.apps.pennypal.util.app_enum.ShowDataPeriod
import com.indie.apps.pennypal.util.internanal.method.getCategoryColorById
import com.indie.apps.pennypal.util.internanal.method.getColorFromId
import com.indie.apps.pennypal.util.internanal.method.getDateFromMillis
import com.indie.apps.pennypal.util.internanal.method.getFirstCharacterUppercase
import java.text.SimpleDateFormat
import kotlin.enums.EnumEntries

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
            verticalAlignment = Alignment.CenterVertically, modifier = modifier
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

@Composable
fun OverviewData(
    currentPeriod: ShowDataPeriod?,
    data: Total?,
    //currency: String,
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
    onSetBudgetClick: (Int) -> Unit,
    onTransactionClick: (Long) -> Unit,
    onMerchantClick: (Long) -> Unit,
    merchantDataAnimType: AnimationType = AnimationType.NONE,
    merchantAnim: AnimationType = AnimationType.NONE,
    isSelectionEnable: Boolean,
    merchantDataAnimId: Long = -1L,
    merchantAnimId: Long = -1L,
    onMerchantDataAnimStop: (AnimationType) -> Unit,
    onMerchantAnimStop: (AnimationType) -> Unit,
    onAddMerchant: () -> Unit
) {

    /*PeriodText(
        text = currentPeriod,
        textStyle = MyAppTheme.typography.Regular51,
    )*/

    OverviewBalanceView(
        currentPeriod = currentPeriod,
        data = data,
    )

    OverviewTransactionData(
        onTransactionClick = onTransactionClick,
        recentTransaction = recentTransaction,
        onSeeAllTransactionClick = onSeeAllTransactionClick,
        merchantDataAnimType = merchantDataAnimType,
        merchantDataAnimId = merchantDataAnimId,
        onAddAnimStop = onMerchantDataAnimStop,
        //currency = currency
    )

    OverviewMerchantData(
        recentMerchant = recentMerchant,
        onSeeAllMerchantClick = onSeeAllMerchantClick,
        onAddMerchant = onAddMerchant,
        merchantAnim = merchantAnim,
        merchantId = merchantAnimId,
        onAnimStop = onMerchantAnimStop,
        onMerchantClick = onMerchantClick
    )

    if (categoryList.isNotEmpty()) {
        OverviewAnalysisData(
            currentPeriod = currentPeriod,
            categoryList = categoryList,
            onExploreAnalysisClick = onExploreAnalysisClick
        )
    }

    OverviewBudgetData(
        budgetWithSpentAndCategoryIdList = budgetWithSpentAndCategoryIdList,
        onExploreBudgetClick = onExploreBudgetClick,
        selectBudgetPeriod = selectBudgetPeriod,
        onSelectBudgetPeriod = onSelectBudgetPeriod,
        onSetBudgetClick = onSetBudgetClick,
        isSelectionEnable = isSelectionEnable,
        //currency = currency
    )
}

@Composable
fun OverviewBalanceView(
    currentPeriod: ShowDataPeriod?,
    data: Total?,
    modifier: Modifier = Modifier,
) {

    val balance = if (data == null) {
        0.0
    } else {
        data.totalIncome - data.totalExpense
    }
    val colorStroke = if (balance >= 0) MyAppTheme.colors.greenBg else MyAppTheme.colors.redBg

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
                    val str = when (currentPeriod) {
                        ShowDataPeriod.THIS_MONTH -> stringResource(id = R.string.balance_of_this_month)
                        ShowDataPeriod.THIS_YEAR -> stringResource(id = R.string.balance_of_this_year)
                        ShowDataPeriod.ALL_TIME -> stringResource(id = R.string.balance)
                        null -> stringResource(id = R.string.balance)
                    }
                    Column {
                        CustomText(
                            text = str,
                            style = MyAppTheme.typography.Regular57,
                            color = MyAppTheme.colors.gray1
                        )
                        AutoSizeText(
                            text = data?.let {
                                Util.getFormattedStringWithSymbol(
                                    balance,
                                    it.baseCurrencySymbol
                                )
                            } ?: "",
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
                            amount = data?.let {
                                Util.getFormattedStringWithSymbol(
                                    it.totalIncome,
                                    it.baseCurrencySymbol
                                )
                            } ?: "",
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
                            amount = data?.let {
                                Util.getFormattedStringWithSymbol(
                                    data.totalExpense,
                                    it.baseCurrencySymbol
                                )
                            } ?: "",
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
    merchantDataAnimType: AnimationType = AnimationType.NONE,
    merchantDataAnimId: Long = -1L,
    onAddAnimStop: (AnimationType) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    /* val str = when (currentPeriod) {
         ShowDataPeriod.THIS_MONTH -> stringResource(id = R.string.recent_transaction_of_this_month)
         ShowDataPeriod.THIS_YEAR -> stringResource(id = R.string.recent_transaction_of_this_year)
         ShowDataPeriod.ALL_TIME -> stringResource(id = R.string.recent_transaction)
         null -> stringResource(id = R.string.recent_transaction)
     }*/

    val str = stringResource(id = R.string.recent_transaction)
    OverviewItem(
        title = str,
        onSeeAllClick = onSeeAllTransactionClick,
        content = {
            val scope = rememberCoroutineScope()

            if (recentTransaction.isNotEmpty()) {
                recentTransaction.forEach { item ->

                    val baseColor = MyAppTheme.colors.itemBg

                    val itemAnimateColor = remember {
                        androidx.compose.animation.Animatable(baseColor)
                    }

                    val modifierAnim = if (merchantDataAnimId == item.id) {
                        when (merchantDataAnimType) {
                            AnimationType.ADD -> Modifier.addAnim(scope) {
                                onAddAnimStop(
                                    AnimationType.ADD
                                )
                            }

                            AnimationType.EDIT -> Modifier.editAnim(
                                scope,
                                itemAnimateColor
                            ) { onAddAnimStop(AnimationType.EDIT) }

                            AnimationType.DELETE -> Modifier.removeAnim(scope) {
                                onAddAnimStop(
                                    AnimationType.DELETE
                                )
                            }

                            else -> Modifier
                        }
                    } else Modifier

                    TransactionItem(
                        item = item,
                        isSelected = false,
                        onClick = {
                            onTransactionClick(item.id)
                        },
                        onLongClick = { },
                        itemBgColor = itemAnimateColor.value,
                        modifier = modifierAnim,
                        //currency = currency
                    )
                }
            } else {
                NoDataMessage(
                    title = stringResource(id = R.string.no_transaction),
                    details = stringResource(id = R.string.no_data_with_transaction1),
                    iconSize = 50.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.padding))
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
    onAddMerchant: () -> Unit,
    merchantAnim: AnimationType = AnimationType.NONE,
    merchantId: Long = -1L,
    onAnimStop: (AnimationType) -> Unit,
    onMerchantClick: (Long) -> Unit,
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
                val scope = rememberCoroutineScope()

                recentMerchant.forEach { item ->

                    val modifierAnim = if (merchantId == item.id) {
                        when (merchantAnim) {
                            AnimationType.ADD -> Modifier.addAnim(scope) {
                                onAnimStop(
                                    AnimationType.ADD
                                )
                            }

                            else -> Modifier
                        }
                    } else Modifier

                    MerchantItem(
                        item = item,
                        modifier = modifierAnim,
                        onClick = {
                            onMerchantClick(item.id)
                        }

                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(5.dp)
                ) {
                    RoundImage(
                        imageVector = Icons.Default.Add,
                        imageVectorSize = 30.dp,
                        tint = MyAppTheme.colors.gray2,
                        backGround = MyAppTheme.colors.bottomBg,
                        contentDescription = "add",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .clickableWithNoRipple { onAddMerchant() })

                }

                repeat(4 - recentMerchant.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

        },
        modifier = modifier
    )
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun OverviewAnalysisData(
    currentPeriod: ShowDataPeriod?,
    onExploreAnalysisClick: () -> Unit,
    categoryList: List<CategoryAmount>,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val str = when (currentPeriod) {
        ShowDataPeriod.THIS_MONTH -> stringResource(id = R.string.analysis_of_this_month)
        ShowDataPeriod.THIS_YEAR -> stringResource(id = R.string.analysis_of_this_year)
        ShowDataPeriod.ALL_TIME -> stringResource(id = R.string.analysis)
        null -> stringResource(id = R.string.analysis)
    }
    OverviewItem(
        title = str,
        trailingText = R.string.explore,
        onSeeAllClick = onExploreAnalysisClick,
        content = {
            val chartData = categoryList.map { item ->
                ChartData(
                    name = item.name,
                    amount = item.amount,
                    color = getCategoryColorById(item.iconColorId)
                )
            }

            Row(
                modifier = Modifier.clickableWithNoRipple(
                    // interactionSource = MutableInteractionSource(),
                    // indication = null
                ) { onExploreAnalysisClick() }) {
                PieChart(
                    data = chartData, modifier = Modifier.align(Alignment.CenterVertically)
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

@SuppressLint("SimpleDateFormat", "UnrememberedMutableInteractionSource")
@Composable
fun OverviewBudgetData(
    budgetWithSpentAndCategoryIdList: BudgetWithSpentAndCategoryIdList?,
    //currency: String,
    onExploreBudgetClick: () -> Unit,
    selectBudgetPeriod: PeriodType,
    onSelectBudgetPeriod: (PeriodType) -> Unit,
    onSetBudgetClick: (Int) -> Unit,
    isSelectionEnable: Boolean,
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
                            tmpBudgetData.startDate, monthFormat
                        )

                        PeriodType.YEAR.id -> getDateFromMillis(
                            tmpBudgetData.startDate, yearFormat
                        )

                        else -> ""
                    }
                }

                if (isSelectionEnable) {
                    OverviewBudgetSelectionButton(
                        list = PeriodType.entries,
                        selectBudgetPeriod = PeriodType.entries.first { it == selectBudgetPeriod },
                        onSelect = onSelectBudgetPeriod
                    )
                }

                SingleBudgetOverAllAnalysis(
                    totalBudgetAmount = budgetWithSpentAndCategoryIdList.budgetAmount,
                    spentAmount = budgetWithSpentAndCategoryIdList.spentAmount,
                    timeString = timeString,
                    currency = budgetWithSpentAndCategoryIdList.originalAmountSymbol
                )

                if (!isSelectionEnable) {
                    val btnTextId =
                        if (budgetWithSpentAndCategoryIdList.periodType == PeriodType.MONTH.id) R.string.add_yearly_budget
                        else R.string.add_monthly_budget

                    val periodId =
                        if (budgetWithSpentAndCategoryIdList.periodType == PeriodType.MONTH.id) PeriodType.YEAR.id
                        else PeriodType.MONTH.id

                    PrimaryButton(
                        onClick = { onSetBudgetClick(periodId) },
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.item_inner_padding))
                    ) {
                        CustomText(
                            text = stringResource(id = btnTextId),
                            style = MyAppTheme.typography.Regular44,
                            color = MyAppTheme.colors.gray0,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

            } else {
                OverviewNoBudgetItem(onSetBudgetClick = { onSetBudgetClick(PeriodType.MONTH.id) })
            }

        },
        isBgEnable = true,
        modifier = modifier.clickableWithNoRipple(
            // interactionSource = MutableInteractionSource(),
            //  indication = null
        ) { onExploreBudgetClick() })
}

@Composable
private fun OverviewNoBudgetItem(
    onSetBudgetClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.75f),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_inner_padding))
        ) {

            CustomText(
                text = stringResource(id = R.string.no_budget_title),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.gray1
            )
            CustomText(
                text = stringResource(id = R.string.no_budget_details),
                style = MyAppTheme.typography.Regular44,
                color = MyAppTheme.colors.gray2
            )

            PrimaryButton(onClick = onSetBudgetClick) {
                CustomText(
                    text = stringResource(id = R.string.set_up_budget),
                    style = MyAppTheme.typography.Regular44,
                    color = MyAppTheme.colors.gray0,
                    textAlign = TextAlign.Center
                )
            }

        }

        Icon(
            imageVector = Icons.Filled.AttachMoney,
            contentDescription = "no transaction",
            tint = MyAppTheme.colors.gray2,
            modifier = Modifier.size(50.dp)
        )
    }
}

@Composable
fun OverviewAnalyticDataItem(
    name: String, color: Color, modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = modifier
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
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(5.dp)
            .clickableWithNoRipple { onClick() }
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
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(55.dp)
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

@SuppressLint("UnrememberedMutableInteractionSource")
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
        modifier = modifier.fillMaxWidth()
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
                    modifier = Modifier.clickableWithNoRipple(
                        //  interactionSource = MutableInteractionSource(), indication = null
                    ) { onSeeAllClick() }) {
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
                modifier = Modifier.roundedCornerBackground(MyAppTheme.colors.itemBg)
            ) {
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
) {

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = modifier
    ) {
        /*PrimaryButton(
            bgColor = MyAppTheme.colors.white,
            borderStroke = BorderStroke(
                width = 1.dp, color = MyAppTheme.colors.gray1
            ),
            onClick = onClick,
            enabled = false,
            modifier = Modifier.size(dimensionResource(R.dimen.top_bar_profile))
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile",
                tint = MyAppTheme.colors.gray1
            )
        }*/

        UserProfileRect()

        Spacer(modifier = Modifier.width(15.dp))

        CustomText(
            text = user?.name ?: "",
            style = MyAppTheme.typography.Regular57,
            color = MyAppTheme.colors.black
        )

        Spacer(modifier = Modifier.weight(1f))


        /* Row(
             verticalAlignment = Alignment.CenterVertically
         ) {
             Checkbox(checked = isSubscribed, onCheckedChange = onSubscriptionChanged)
             CustomText(
                 text = "Subscribed",
                 style = MyAppTheme.typography.Regular57,
                 color = MyAppTheme.colors.black
             )
         }*/

    }
}

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
            .fillMaxWidth(), horizontalArrangement = Arrangement.End
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