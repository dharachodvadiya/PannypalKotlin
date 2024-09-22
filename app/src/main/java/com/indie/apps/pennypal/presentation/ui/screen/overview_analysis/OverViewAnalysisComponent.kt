package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.CategoryAmount
import com.indie.apps.pennypal.data.module.ChartData
import com.indie.apps.pennypal.presentation.ui.component.chart.PieChart
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.GetCategoryColor
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getCategoryIcon
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorPosition
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties

@Composable
fun OverViewAnalysisPeriod(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .roundedCornerBackground(MyAppTheme.colors.brand)
            .padding(
                horizontal = dimensionResource(R.dimen.bottom_bar_item_horizontal_padding),
                vertical = dimensionResource(R.dimen.bottom_bar_item_vertical_padding)
            )
    ) {
        CustomText(
            text = stringResource(id = R.string.this_month),
            style = MyAppTheme.typography.Regular44,
            color = MyAppTheme.colors.black
        )
    }
}

val columnGridProperties = GridProperties(
    enabled = true,
    xAxisProperties = GridProperties.AxisProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .6f))
    ),
    yAxisProperties = GridProperties.AxisProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .6f))
    ),
)

@Composable
fun OverViewAnalysisIncExpChart(
    modifier: Modifier = Modifier
) {
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
            color = GetCategoryColor(item.name)
        )
    }

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

@Composable
fun CategoryListItem(
    item: CategoryAmount,
    itemBgColor: Color,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = ImageVector.vectorResource(getCategoryIcon(item.name))
    val tintColor = GetCategoryColor(item.name)

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