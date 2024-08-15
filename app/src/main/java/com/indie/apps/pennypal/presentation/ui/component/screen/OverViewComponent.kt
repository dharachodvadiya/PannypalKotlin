package com.indie.apps.pennypal.presentation.ui.component.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MerchantDataDailyTotal
import com.indie.apps.pennypal.data.module.MerchantDataWithName
import com.indie.apps.pennypal.presentation.ui.common.Util
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.AutoSizeText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun OverviewTopBar(
    //onSearchTextChange: (String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    /*var isSearch by remember { mutableStateOf(false) }

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
        leadingContent = { OverviewTopBarProfile(onClick = onProfileClick) },
        modifier = modifier
    )
}

@Composable
fun OverviewBalanceView(
    balance: Double,
    modifier: Modifier = Modifier
) {

    val colorStroke = if (balance >= 0) MyAppTheme.colors.greenBg else MyAppTheme.colors.redBg
    val colorGradient =
        if (balance >= 0) MyAppTheme.colors.gradientGreen else MyAppTheme.colors.gradientRed
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            //.background(brush = verticalGradientsBrush(colorGradient))
            .padding(dimensionResource(id = R.dimen.padding))
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
            color = MyAppTheme.colors.lightBlue2,
            shadowElevation = dimensionResource(id = R.dimen.shadow_elevation)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawLine(
                            colorStroke,
                            Offset(0f, 0f),
                            Offset(size.width, 0f),
                            25f
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.balance),
                        style = MyAppTheme.typography.Semibold67_5,
                        color = MyAppTheme.colors.gray0
                    )
                    AutoSizeText(
                        text = Util.getFormattedStringWithSymbol(balance),
                        style = MyAppTheme.typography.Regular77_5,
                        color = MyAppTheme.colors.black,
                        maxLines = 2,
                        modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding))
                    )
                }
            }

        }
    }


}

@Composable
fun OverviewList(
    dataList: LazyPagingItems<MerchantDataWithName>,
    dailyTotalList: LazyPagingItems<MerchantDataDailyTotal>,
    modifier: Modifier = Modifier,
    isLoadMore: Boolean = false,
    bottomPadding: PaddingValues
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_padding)),
        contentPadding = bottomPadding
    ) {
        /*items(10) { index ->

            OverviewListItem(
                isDateShow = (index % 3 == 0)
            )
        }
*/
        var totalListIndex = 0
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

                Column() {
                    if (isDateShow) {
                        if (index != 0)
                            Spacer(modifier = Modifier.height(13.dp))
                        dailyTotalList[totalListIndex]?.let { OverviewListDateItem(it) }
                        totalListIndex++
                    }
                    OverviewListItem(
                        item = data
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
        }
    }
}

@Composable
fun OverviewListDateItem(
    item: MerchantDataDailyTotal,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val dayString = when (item.day) {
        Util.getTodayDate() -> stringResource(id = R.string.today)
        Util.getYesterdayDate() -> stringResource(id = R.string.yesterday)
        else -> item.day
    }
    val amount = item.totalIncome - item.totalExpense
    val totalTextColor = if (amount >= 0) MyAppTheme.colors.greenText else MyAppTheme.colors.redText
    Column(modifier = modifier) {
        Text(
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

}

@Composable
fun OverviewListItem(
    item: MerchantDataWithName,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = if (item.type < 0) Icons.Default.NorthEast else Icons.Default.SouthWest
    val bgColor = if (item.type < 0) MyAppTheme.colors.redBg else MyAppTheme.colors.greenBg

    ListItem(
        isClickable = false,
        leadingIcon = {
            RoundImage(
                imageVector = imageVector,
                tint = MyAppTheme.colors.black,
                backGround = bgColor,
                contentDescription = "amount"
            )
        },
        content = {
            Column {
                Text(
                    text = item.merchantName,
                    style = MyAppTheme.typography.Semibold52_5,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (item.details.isNullOrEmpty()) stringResource(id = R.string.no_details) else item.details,
                    style = MyAppTheme.typography.Medium33,
                    color = MyAppTheme.colors.gray1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        trailingContent = {
            Text(
                text = Util.getFormattedStringWithSymbol(if (item.type > 0) item.amount else item.amount * -1),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.black,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.5f),
                textAlign = TextAlign.Right,
                maxLines = 1
            )
        },
        isSetDivider = false,
        modifier = modifier
    )
}

@Composable
private fun OverviewTopBarProfile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
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
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        MyAppTheme.colors.gray1
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                )
                .background(MyAppTheme.colors.white)
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Profile"
            )
        }


    }
}

@Composable
fun OverviewAppFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryButton(
        //bgBrush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
        bgColor = MyAppTheme.colors.buttonBg,
        onClick = onClick,
        modifier = modifier
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
}

@Preview(showBackground = true)
@Composable
private fun TopBarProfilePreview() {
    PennyPalTheme(darkTheme = true) {
        OverviewTopBarProfile(onClick = { })
    }
}

@Preview
@Composable
private fun OverviewListItemPreview() {
    PennyPalTheme(darkTheme = true) {
        // OverviewListItem()
    }
}