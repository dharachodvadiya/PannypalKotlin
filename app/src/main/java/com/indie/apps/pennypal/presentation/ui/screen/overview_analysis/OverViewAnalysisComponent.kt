package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.enum.AnalysisPeriod
import com.indie.apps.pennypal.data.module.ChartData
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.presentation.ui.component.chart.PieChart
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomTab
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getCategoryColorById
import com.indie.apps.pennypal.util.getCategoryIconById
import kotlin.enums.EnumEntries

@Composable
fun AnalysisTopSelectionButton(
    list: EnumEntries<AnalysisPeriod>,
    selectedPeriod: AnalysisPeriod,
    onSelect: (AnalysisPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {

    val tabItems = list.map { period ->
        TabItemInfo(
            title = when (period) {
                AnalysisPeriod.MONTH -> R.string.month
                AnalysisPeriod.YEAR -> R.string.year
            },
            selectBgColor = MyAppTheme.colors.itemSelectedBg,
            unSelectBgColor = MyAppTheme.colors.itemBg,
            selectContentColor = MyAppTheme.colors.black,
            unSelectContentColor = MyAppTheme.colors.gray1
        )
    }

    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CustomTab(tabList = tabItems,
            selectedIndex = list.indexOf(selectedPeriod),
            onTabSelected = {
                onSelect(list[it])
            })

    }

}

@Composable
fun AnalysisMonthYearSelection(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onTextClick: () -> Unit,
    textYearMonth: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        /*Icon(
            modifier = Modifier
                .size(35.dp)
                .clickableWithNoRipple {
                    onPreviousClick()
                },
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "previous",
            tint = MyAppTheme.colors.black
        )*/

        RoundImage(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            backGround = MyAppTheme.colors.itemSelectedBg.copy(alpha = 0.5f),
            tint = MyAppTheme.colors.black,
            contentDescription = "previous",
            innerPadding = 5.dp,
            modifier = Modifier
                .clickableWithNoRipple {
                    onPreviousClick()
                },
        )
        Box(
            modifier = Modifier
                .height(34.dp)
                .roundedCornerBackground(MyAppTheme.colors.itemSelectedBg.copy(alpha = 0.5f))
                .clickableWithNoRipple { onTextClick() }
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            CustomText(
                text = textYearMonth,
                textAlign = TextAlign.Center,
                color = MyAppTheme.colors.black
            )
        }


        /* Icon(
             modifier = Modifier
                 .size(35.dp)
                 .clickableWithNoRipple {
                     onNextClick()
                 },
             imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
             contentDescription = "next",
             tint = MyAppTheme.colors.black
         )*/

        RoundImage(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            backGround = MyAppTheme.colors.itemSelectedBg.copy(alpha = 0.5f),
            tint = MyAppTheme.colors.black,
            contentDescription = "previous",
            innerPadding = 5.dp,
            modifier = Modifier

                .clickableWithNoRipple {
                    onNextClick()
                },
        )

    }
}

@Composable
fun AnalysisBalance(
    receiveAmount: Double,
    spentAmount: Double,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val remainAmount = receiveAmount - spentAmount

    Column(
        modifier = modifier
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.defaultMinSize(minWidth = 150.dp)
            ) {
                CustomText(
                    text = stringResource(R.string.balance),
                    style = MyAppTheme.typography.Regular57,
                    color = MyAppTheme.colors.gray2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis

                )
                CustomText(
                    text = Util.getFormattedStringWithSymbol(remainAmount),
                    style = MyAppTheme.typography.Regular57,
                    color = if (remainAmount < 0) MyAppTheme.colors.redBg else MyAppTheme.colors.greenBg,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding)))

                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = modifier
                ) {
                    RoundImage(
                        imageVector = Icons.Default.SouthWest,
                        tint = MyAppTheme.colors.gray2,
                        backGround = MyAppTheme.colors.itemSelectedBg.copy(alpha = 0.3f),
                        contentDescription = "amount",
                        imageVectorSize = 13.dp,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    CustomText(
                        text = stringResource(R.string.received),
                        style = MyAppTheme.typography.Regular46,
                        color = MyAppTheme.colors.gray2
                    )
                }
                CustomText(
                    text = Util.getFormattedStringWithSymbol(receiveAmount),
                    style = MyAppTheme.typography.Regular57,
                    color = MyAppTheme.colors.black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding)))

                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = modifier
                ) {
                    RoundImage(
                        imageVector = Icons.Default.NorthEast,
                        tint = MyAppTheme.colors.gray2,
                        backGround = MyAppTheme.colors.itemSelectedBg.copy(alpha = 0.3f),
                        contentDescription = "amount",
                        imageVectorSize = 13.dp,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    CustomText(
                        text = stringResource(R.string.spent),
                        style = MyAppTheme.typography.Regular46,
                        color = MyAppTheme.colors.gray2
                    )
                }
                CustomText(
                    text = Util.getFormattedStringWithSymbol(spentAmount),
                    style = MyAppTheme.typography.Regular57,
                    color = MyAppTheme.colors.black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val chartData = mutableListOf<ChartData>()
                chartData.add(
                    ChartData(
                        name = "Spent",
                        amount = spentAmount,
                        color = MyAppTheme.colors.redBg
                    )
                )
                if (remainAmount > 0) {
                    chartData.add(
                        ChartData(
                            name = "Remain",
                            amount = remainAmount,
                            color = MyAppTheme.colors.greenBg
                        )
                    )
                }

                PieChart(
                    data = chartData,
                    radiusOuter = 50.dp,
                    chartBarWidth = 30.dp
                )
            }
        }
    }
}


@Composable
fun OverViewAnalysisCategoryChart(
    categoryList: List<CategoryAmount>,
    modifier: Modifier = Modifier
) {
    val chartData = categoryList.map { item ->
        ChartData(
            name = item.name,
            amount = item.amount,
            color = getCategoryColorById(item.iconColorId)
        )
    }

    Column(
        modifier = modifier
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            .padding(
                vertical = dimensionResource(R.dimen.padding),
                horizontal = dimensionResource(R.dimen.item_inner_padding)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText(
            text = stringResource(R.string.category_wise_spending),
            style = MyAppTheme.typography.Regular51,
            color = MyAppTheme.colors.gray1,
            modifier = Modifier.fillMaxWidth()
        )

        PieChart(
            data = chartData
        )

        Column {
            categoryList.forEach { item ->
                CategoryListItem(
                    item = item,
                    itemBgColor = MyAppTheme.colors.transparent
                )
            }
        }
    }


}

@Composable
private fun CategoryListItem(
    item: CategoryAmount,
    itemBgColor: Color,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector =
        ImageVector.vectorResource(getCategoryIconById(item.iconId, LocalContext.current))
    val tintColor = getCategoryColorById(item.iconColorId)

    ListItem(
        isClickable = false,
        leadingIcon = {
            RoundImage(
                imageVector = imageVector,
                imageVectorSize = 20.dp,
                //brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                tint = tintColor,
                backGround = MyAppTheme.colors.lightBlue2,
                contentDescription = "item",
                modifier = Modifier.size(40.dp)
            )

        },
        content = {
            CustomText(
                text = item.name,
                style = MyAppTheme.typography.Semibold52_5,
                color = MyAppTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            CustomText(
                text = Util.getFormattedStringWithSymbol(item.amount),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.black,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.5f),
                textAlign = TextAlign.Right,
                maxLines = 1
            )

        },
        modifier = modifier,
        itemBgColor = itemBgColor
    )
}


@Preview
@Composable
private fun BudgetListItemPreview() {
    PennyPalTheme(darkTheme = true) {
        AnalysisBalance(
            receiveAmount = 100.0,
            spentAmount = 20.0,
        )
    }
}