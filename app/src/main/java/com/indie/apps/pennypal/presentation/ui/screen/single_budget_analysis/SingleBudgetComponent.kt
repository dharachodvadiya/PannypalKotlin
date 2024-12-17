package com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.ChartData
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.chart.PieChart
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomProgressItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getCategoryColor
import com.indie.apps.pennypal.util.getCategoryIcon

@Composable
fun SingleBudgetTopBar(
    title: String = "",
    onNavigationUp: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TopBarWithTitle(
        title = title, onNavigationUp = {
            onNavigationUp()
        }, contentAlignment = Alignment.Center,
        trailingContent = {

            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete_top),
                    contentDescription = "Delete",
                    tint = MyAppTheme.colors.black,
                    modifier = Modifier
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .size(25.dp)
                        .clickable { onDeleteClick() }
                )
                Spacer(modifier = Modifier.width(15.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "edit",
                    tint = MyAppTheme.colors.black,
                    modifier = Modifier
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .size(25.dp)
                        .clickable { onEditClick() }
                )
            }

        }, modifier = modifier
    )
}

@Composable
fun SingleBudgetOverAllAnalysis(
    totalBudgetAmount: Double,
    spentAmount: Double,
    timeString: String,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val remainAmount = totalBudgetAmount - spentAmount

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
                    text = timeString,
                    style = MyAppTheme.typography.Regular57,
                    color = MyAppTheme.colors.black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis

                )
                CustomText(
                    text = "${stringResource(R.string.budget)} : ${
                        Util.getFormattedStringWithSymbol(
                            totalBudgetAmount
                        )
                    }",
                    style = MyAppTheme.typography.Medium40,
                    color = MyAppTheme.colors.gray2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding)))

                CustomText(
                    text = stringResource(R.string.total_spent),
                    style = MyAppTheme.typography.Medium40,
                    color = MyAppTheme.colors.gray2,
                )
                CustomText(
                    text = Util.getFormattedStringWithSymbol(spentAmount),
                    style = MyAppTheme.typography.Regular57,
                    color = MyAppTheme.colors.black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding)))

                CustomText(
                    text = stringResource(R.string.available_budget),
                    style = MyAppTheme.typography.Medium40,
                    color = MyAppTheme.colors.gray2,
                )
                CustomText(
                    text = Util.getFormattedStringWithSymbol(remainAmount),
                    style = MyAppTheme.typography.Regular57,
                    color = if (remainAmount < 0) MyAppTheme.colors.redBg else MyAppTheme.colors.black,
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
                        color = if (remainAmount < 0) MyAppTheme.colors.redBg else MyAppTheme.colors.lightBlue1
                    )
                )
                if (remainAmount > 0) {
                    chartData.add(
                        ChartData(
                            name = "Remain",
                            amount = totalBudgetAmount - spentAmount,
                            color = MyAppTheme.colors.gray1.copy(0.2f)
                        )
                    )
                }

                PieChart(
                    data = chartData,
                    radiusOuter = 50.dp,
                    chartBarWidth = 20.dp
                )
            }
        }

        HorizontalDivider()
        Row(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding),
                    vertical = dimensionResource(id = R.dimen.item_inner_padding)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AutoGraph,
                contentDescription = "edit",
                tint = MyAppTheme.colors.gray2,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            CustomText(
                text = stringResource(if (remainAmount < 0) R.string.exceed_budget_message else R.string.under_budget_message),
                style = MyAppTheme.typography.Regular44,
                color = MyAppTheme.colors.gray2,
            )
        }
    }
}

@Composable
fun BudgetedCategoryAnalysis(
    categoryLimitList: List<CategoryAmount>,
    categorySpentList: List<CategoryAmount>,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val count = categoryLimitList.count { it.amount > 0 }

    if (count > 0) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .roundedCornerBackground(MyAppTheme.colors.itemBg)
                .padding(dimensionResource(id = R.dimen.item_inner_padding))
        ) {
            CustomText(
                text = stringResource(id = R.string.budgeted_category),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.gray1
            )

            categoryLimitList.forEach { item ->

                if (item.amount > 0) {
                    CustomProgressItem(
                        name = item.name,
                        totalAmount = item.amount,
                        spentAmount = categorySpentList.firstOrNull { it.id == item.id }?.amount
                            ?: 0.0,
                        isClickable = false
                    )
                }

            }
        }
    }

}

@Composable
fun IncludedCategoryAnalysis(
    categoryLimitList: List<CategoryAmount>,
    categorySpentList: List<CategoryAmount>,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .roundedCornerBackground(MyAppTheme.colors.itemBg)
            .padding(dimensionResource(id = R.dimen.item_inner_padding))
    ) {
        CustomText(
            text = stringResource(id = R.string.included_category),
            style = MyAppTheme.typography.Regular51,
            color = MyAppTheme.colors.gray1
        )

        val categoryList = mutableListOf<CategoryAmount>()

        categoryLimitList.forEach { item ->
            val spentItem = categorySpentList.firstOrNull { it.id == item.id }
            if (spentItem != null) {
                categoryList.add(spentItem)
            } else {
                categoryList.add(item)
            }
        }

        Column {
            categoryList.sortedByDescending { it.amount }.forEach { item ->
                CategorySpentListItem(
                    totalAmount = categoryList.first().amount,
                    item = item,
                    itemBgColor = MyAppTheme.colors.transparent
                )
            }
        }
    }
}

@Composable
private fun CategorySpentListItem(
    totalAmount: Double,
    item: CategoryAmount,
    itemBgColor: Color,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = ImageVector.vectorResource(getCategoryIcon(item.name))
    val tintColor = getCategoryColor(item.name)

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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                CustomText(
                    text = item.name,
                    style = MyAppTheme.typography.Semibold52_5,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Progress Bar
                val totalBudgetAmount = totalAmount

                // Calculate the percentage of the budget spent
                val progress = if (totalBudgetAmount > 0) {
                    (item.amount / totalBudgetAmount).coerceIn(0.0, 1.0)
                        .toFloat() // Ensure it stays within 0 to 1 range
                } else {
                    0.0F
                }

                // Display progress bar
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(2.dp))
                        .height(5.dp),
                    color = getCategoryColor(item.name),
                    trackColor = MyAppTheme.colors.gray1.copy(0.2f),
                )
            }

        },
        trailingContent = {
            CustomText(
                text = Util.getFormattedStringWithSymbol(item.amount),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.black,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(70.dp),
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
        SingleBudgetOverAllAnalysis(
            totalBudgetAmount = 100.0,
            spentAmount = 20.0,
            timeString = "November 2024"
        )
    }
}