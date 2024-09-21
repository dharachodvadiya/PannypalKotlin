package com.indie.apps.pennypal.presentation.ui.screen.merchant_data

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MerchantDataWithPaymentName
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.AutoSizeText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import java.text.SimpleDateFormat

@Composable
fun MerchantDataTopBar(
    selectCount: Int = 0,
    name: String = "",
    description: String = "",
    onClick: () -> Unit,
    onCloseClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onNavigationUp: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TopBar(
        isBackEnable = selectCount == 0,
        onBackClick = onNavigationUp,
        content = {
            MerchantDataTopBarItem(
                selectCount = selectCount,
                name = name,
                description = description,
                onClick = onClick,
                onCloseClick = onCloseClick
            )
        },
        modifier = modifier,
        trailingContent = {
            if (selectCount == 0) {
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
            }
        }
    )
}

@Composable
private fun MerchantDataTopBarItem(
    selectCount: Int = 0,
    name: String = "",
    description: String = "",
    onClick: () -> Unit,
    onCloseClick: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = Icons.Default.Person
    val bgColor = MyAppTheme.colors.lightBlue2


    ListItem(
        onClick = onClick,
        leadingIcon = {
            if (selectCount > 0) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .clickable {
                            onCloseClick()
                        }
                )
            } else {
                RoundImage(
                    imageVector = imageVector,
                    //brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                    tint = MyAppTheme.colors.black,
                    backGround = bgColor,
                    contentDescription = "person",
                    imageVectorSize = 27.dp
                )
            }
        },
        content = {
            Column {
                if (selectCount > 0) {
                    CustomText(
                        text = "$selectCount ${stringResource(id = R.string.selected_text)}",
                        style = MyAppTheme.typography.Semibold52_5,
                        color = MyAppTheme.colors.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    CustomText(
                        text = name.ifEmpty { stringResource(id = R.string.no_name) },
                        style = MyAppTheme.typography.Semibold52_5,
                        color = MyAppTheme.colors.black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (description.isNotEmpty()) {
                        CustomText(
                            text = description,
                            style = MyAppTheme.typography.Medium44,
                            color = MyAppTheme.colors.gray2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }

            }
        },
        modifier = modifier,
        itemBgColor = MyAppTheme.colors.transparent
    )
}

@Composable
fun MerchantDataBottomBar(
    //totalIncome: Double = 0.0,
    //totalExpense: Double = 0.0,
    isEditable: Boolean = false,
    isDeletable: Boolean = false,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    if (isEditable || isDeletable) {
        Row(
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding))
        ) {
            if (isEditable) {
                MerchantDataBottomButton(
                    text = R.string.edit_transaction,
                    painterRes = R.drawable.ic_edit,
                    //bgBrush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                    bgColor = MyAppTheme.colors.buttonBg,
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f)
                )
            }

            if (isDeletable) {
                Spacer(modifier = Modifier.width(7.dp))
                MerchantDataBottomButton(
                    text = R.string.delete_transaction,
                    painterRes = R.drawable.ic_delete_top,
                    bgColor = MyAppTheme.colors.redBg,
                    onClick = onDeleteClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }

    } else {
        /* MerchantDataBottomTotal(
             totalIncome = totalIncome,
             totalExpense = totalExpense,
             modifier = modifier
                 .padding(
                     top = 7.dp,
                     bottom = 0.dp,
                     start = 0.dp,
                     end = 0.dp
                 )
         )*/
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MerchantDataIncomeAmount(
    isSelected: Boolean = false,
    data: MerchantDataWithPaymentName,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    itemBgColor: Color,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val bgColor =
        if (isSelected) MyAppTheme.colors.itemSelectedBg else MyAppTheme.colors.transparent

    Row(
        modifier = modifier
            .roundedCornerBackground(bgColor)
            //.background(bgColor)
            // .padding(vertical = 2.dp)
            //.clip(RoundedCornerShape(dimensionResource(R.dimen.round_corner)))
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                role = Role.Button
            )
            .padding(vertical = 5.dp)

    ) {

        MerchantDataAmountItem(
            amount = data.amount,
            description = data.details,
            contentAlignment = Alignment.Start,
            colorStroke = MyAppTheme.colors.greenBg,
            itemBgColor = itemBgColor,
            paymentName = data.paymentName
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MerchantDataExpenseAmount(
    isSelected: Boolean = false,
    data: MerchantDataWithPaymentName,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    itemBgColor: Color,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val bgColor =
        if (isSelected) MyAppTheme.colors.itemSelectedBg else MyAppTheme.colors.transparent

    Row(
        modifier = modifier
            .roundedCornerBackground(bgColor)
            //.background(bgColor)
            .fillMaxWidth()
            //.padding(vertical = 2.dp)
            //.clip(RoundedCornerShape(dimensionResource(R.dimen.round_corner)))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                role = Role.Button
            )
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.End
    ) {

        MerchantDataAmountItem(
            amount = data.amount * -1,
            description = data.details,
            contentAlignment = Alignment.End,
            colorStroke = MyAppTheme.colors.redBg,
            itemBgColor = itemBgColor,
            paymentName = data.paymentName
        )

    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun MerchantDataDateItem(
    dateMillis: Long = 0,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val format = SimpleDateFormat("dd MMMM yyyy")

    val dayString = when (val day = Util.getDateFromMillis(dateMillis, format)) {
        Util.getTodayDate(format) -> stringResource(id = R.string.today)
        Util.getYesterdayDate(format) -> stringResource(id = R.string.yesterday)
        else -> day
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(color = MyAppTheme.colors.gray2)
        )
        CustomText(
            text = dayString,
            style = MyAppTheme.typography.Medium40,
            color = MyAppTheme.colors.gray2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 3.dp)
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(color = MyAppTheme.colors.gray2)
        )
    }
}

@Composable
private fun MerchantDataAmountItem(
    amount: Double,
    description: String? = null,
    paymentName: String,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    contentAlignment: Alignment.Horizontal,
    colorStroke: Color = MyAppTheme.colors.black,
    itemBgColor: Color
) {
    /*Surface(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
            .widthIn(min = 150.dp, max = 250.dp),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
        color = itemBgColor
    )
    {
        Column(
            modifier = modifier
                .drawBehind {
                    drawLine(
                        colorStroke,
                        Offset(0f, size.height),
                        Offset(size.width, size.height),
                        25f
                    )
                }
                .padding(10.dp),
            //.padding(horizontal = 10.dp),
            horizontalAlignment = contentAlignment
        ) {
            AutoSizeText(
                text = Util.getFormattedStringWithSymbol(amount),
                style = MyAppTheme.typography.Regular51,
                color = MyAppTheme.colors.black,
                maxLines = 2,
            )
            CustomText(
                text = "${stringResource(R.string.by)} $paymentName",
                style = MyAppTheme.typography.Medium34,
                color = MyAppTheme.colors.gray0,
                maxLines = 1,
            )
            if (!description.isNullOrEmpty()) {
                CustomText(
                    text = description,
                    style = MyAppTheme.typography.Medium34,
                    color = MyAppTheme.colors.gray2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }*/

    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
            .widthIn(min = 150.dp, max = 250.dp)
            .roundedCornerBackground(itemBgColor)
            .drawBehind {
                drawLine(
                    colorStroke,
                    Offset(0f, size.height),
                    Offset(size.width, size.height),
                    25f
                )
            }
            .padding(10.dp),
        //.padding(horizontal = 10.dp),
        horizontalAlignment = contentAlignment
    ) {
        AutoSizeText(
            text = Util.getFormattedStringWithSymbol(amount),
            style = MyAppTheme.typography.Regular51,
            color = MyAppTheme.colors.black,
            maxLines = 2,
        )
        CustomText(
            text = "${stringResource(R.string.by)} $paymentName",
            style = MyAppTheme.typography.Medium34,
            color = MyAppTheme.colors.gray0,
            maxLines = 1,
        )
        if (!description.isNullOrEmpty()) {
            CustomText(
                text = description,
                style = MyAppTheme.typography.Medium34,
                color = MyAppTheme.colors.gray2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@Composable
private fun MerchantDataBottomButton(
    @StringRes text: Int,
    @DrawableRes painterRes: Int,
    bgBrush: Brush? = null,
    bgColor: Color = MyAppTheme.colors.brand,
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    PrimaryButton(
        bgColor = bgColor,
        bgBrush = bgBrush,
        modifier = modifier
            .height(dimensionResource(id = R.dimen.button_height)),
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(painterRes),
            contentDescription = "",
            tint = MyAppTheme.colors.black,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        CustomText(
            text = stringResource(text),
            style = MyAppTheme.typography.Bold49_5,
            color = MyAppTheme.colors.black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MerchantDataBottomTotal(
    totalIncome: Double = 0.0,
    totalExpense: Double = 0.0,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(69.dp)
            .background(
                shape = RoundedCornerShape(
                    topStart = dimensionResource(id = R.dimen.round_corner),
                    topEnd = dimensionResource(id = R.dimen.round_corner),
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                color = MyAppTheme.colors.bottomBg
            )
            .padding(horizontal = dimensionResource(id = R.dimen.padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MerchantDataTotalIncomeExpense(
            amount = totalIncome,
            modifier = Modifier.weight(1f),
            strokeColor = MyAppTheme.colors.greenBg
        )
        Spacer(modifier = Modifier.width(7.dp))
        MerchantDataTotalIncomeExpense(
            amount = totalExpense,
            modifier = Modifier.weight(1f),
            strokeColor = MyAppTheme.colors.redBg
        )
        Spacer(modifier = Modifier.width(7.dp))
        MerchantDataTotal(amount = totalIncome - totalExpense, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MerchantDataTotalIncomeExpense(
    amount: Double = 0.0,
    strokeColor: Color,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.top_bar_profile))
            .roundedCornerBackground(MyAppTheme.colors.transparent, BorderStroke(2.dp, strokeColor))
        /* .background(
             shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
             color = MyAppTheme.colors.transparent
         )
         .border(
             width = 2.dp,
             color = strokeColor,
             shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
         )*/,
        contentAlignment = Alignment.Center
    ) {
        AutoSizeText(
            text = Util.getFormattedStringWithSymbol(amount),
            style = MyAppTheme.typography.Semibold50,
            color = MyAppTheme.colors.black,
            maxLines = 1,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
private fun MerchantDataTotal(
    amount: Double = 0.0,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        AutoSizeText(
            text = Util.getFormattedStringWithSymbol(amount),
            style = MyAppTheme.typography.Semibold52_5,
            color = MyAppTheme.colors.black,
            maxLines = 1
        )
        CustomText(
            text = stringResource(id = R.string.total_amount),
            style = MyAppTheme.typography.Medium40,
            color = MyAppTheme.colors.gray2
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantDataTopBarPreview() {
    PennyPalTheme(darkTheme = true) {
        MerchantDataTopBar(onClick = { /*TODO*/ }, onNavigationUp = { /*TODO*/ })
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantDataBottomBarPreview() {
    PennyPalTheme(darkTheme = true) {
        MerchantDataBottomBar(
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantDataIncomeAmountItemPreview() {
    PennyPalTheme(darkTheme = true) {
        /*MerchantDataIncomeAmount(
            onClick = {},
            onLongClick = {}
        )*/
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantDataExpenseAmountItemPreview() {
    PennyPalTheme(darkTheme = true) {
        /*MerchantDataExpenseAmount(
            onClick = {},
            onLongClick = {}
        )*/
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantDataDateItemPreview() {
    PennyPalTheme(darkTheme = true) {
        MerchantDataDateItem()
    }
}