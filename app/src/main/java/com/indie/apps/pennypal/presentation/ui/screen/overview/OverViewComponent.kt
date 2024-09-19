package com.indie.apps.pennypal.presentation.ui.screen.overview

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.DailyTotal
import com.indie.apps.pennypal.data.module.MerchantDataWithAllData
import com.indie.apps.pennypal.data.module.MerchantDataWithName
import com.indie.apps.pennypal.data.module.MerchantDataWithNameWithDayTotal
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.data.module.TotalWithCurrency
import com.indie.apps.pennypal.data.module.toMerchantDataDailyTotal
import com.indie.apps.pennypal.data.module.toMerchantDataWithName
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.AutoSizeText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImageWithText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.screen.all_data.TransactionItem
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.GetColorFromId
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getFirstCharacterUppercase
import kotlinx.coroutines.launch

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
        leadingContent = { OverviewTopBarProfile(onClick = onProfileClick) },
        modifier = modifier
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.balance),
                        style = MyAppTheme.typography.Semibold67_5,
                        color = MyAppTheme.colors.gray0
                    )
                    AutoSizeText(
                        text = Util.getFormattedStringWithSymbol(balance, symbol),
                        style = MyAppTheme.typography.Regular77_5,
                        color = MyAppTheme.colors.black,
                        maxLines = 2,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.padding))
                    )
                }
            }

        }
    }


}

@SuppressLint("CoroutineCreationDuringComposition")
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

            /*var totalListIndex = 0
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
            }*/

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
                    }/*when (data) {
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
                    }*/

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
            Text(
                text = item.merchantName,
                style = MyAppTheme.typography.Semibold52_5,
                color = MyAppTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!item.details.isNullOrEmpty()) {
                Text(
                    text = item.details,
                    style = MyAppTheme.typography.Medium33,
                    color = MyAppTheme.colors.gray1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }, trailingContent = {
        Text(
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
}

@Composable
fun OverviewData(
    recentTransaction: List<MerchantDataWithAllData>,
    recentMerchant: List<MerchantNameAndDetails>,
    onSeeAllTransactionClick: () -> Unit,
    onSeeAllMerchantClick: () -> Unit,
    isAddMerchantDataSuccess: Boolean = false,
    merchantDataId: Long = 1L,
    onAnimStop: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    OverviewItem(
        title = R.string.recent_transaction,
        onSeeAllClick = onSeeAllTransactionClick,
        content = {
            val scope = rememberCoroutineScope()

            recentTransaction.forEach { item ->

                val itemAnimateScale = remember {
                    Animatable(0f)
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
                    } else {
                        Modifier
                    }

                TransactionItem(
                    item = item,
                    isSelected = false,
                    onClick = {
                    },
                    onLongClick = { },
                    itemBgColor = MyAppTheme.colors.itemBg,
                    modifier = modifierAdd
                )
            }
        }
    )

    Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.overview_item_padding)))

    OverviewItem(
        title = R.string.recent_merchant,
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

        }
    )
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
            backGround = GetColorFromId(item.id.toInt()),
            modifier = Modifier.size(55.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = item.name,
            style = MyAppTheme.typography.Regular44,
            color = MyAppTheme.colors.black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if (!item.details.isNullOrEmpty()) {
            Text(
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
    @StringRes title: Int,
    content: @Composable (() -> Unit),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = title),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.gray1
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onSeeAllClick() }
            ) {
                Text(
                    text = stringResource(id = R.string.see_all),
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
        
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.item_padding)))

        content()
    }

}

@Composable
private fun OverviewTopBarProfile(
    onClick: () -> Unit, modifier: Modifier = Modifier
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

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(dimensionResource(R.dimen.top_bar_profile))
            .roundedCornerBackground(
                MyAppTheme.colors.white, BorderStroke(width = 1.dp, MyAppTheme.colors.gray1)
            )
            .clickable { onClick() }/*.border(
                border = BorderStroke(
                    width = 1.dp,
                    MyAppTheme.colors.gray1
                ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
            )
            .background(MyAppTheme.colors.white)*/
    ) {
        Icon(
            Icons.Filled.Person, contentDescription = "Profile", tint = MyAppTheme.colors.gray1
        )
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