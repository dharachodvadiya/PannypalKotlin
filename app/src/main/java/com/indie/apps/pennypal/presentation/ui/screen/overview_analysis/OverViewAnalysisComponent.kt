package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.data.module.ChartData
import com.indie.apps.pennypal.data.module.category.CategoryAmount
import com.indie.apps.pennypal.presentation.ui.component.chart.PieChart
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getCategoryColor
import com.indie.apps.pennypal.util.getCategoryIcon


@Composable
fun OverViewAnalysisCategoryChart(
    categoryList: List<CategoryAmount>,
    modifier: Modifier = Modifier
) {
    val chartData = categoryList.map { item ->
        ChartData(
            name = item.name,
            amount = item.amount,
            color = getCategoryColor(item.name)
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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