package com.indie.apps.pennypal.presentation.ui.screen.all_data

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithAllData
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getCategoryColorById
import com.indie.apps.pennypal.util.getCategoryIconById
import com.indie.apps.pennypal.util.getDateFromMillis
import com.indie.apps.pennypal.util.getTodayDate
import com.indie.apps.pennypal.util.getYesterdayDate
import java.text.SimpleDateFormat

@Composable
fun AllDataTopBar(
    title: String = "",
    isSelected: Boolean = false,
    isDeletable: Boolean = false,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNavigationUp: () -> Unit,
    textState: TextFieldState,
    onSearchTextChange: (String) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TopBarWithTitle(
        isBackEnable = true,
        onNavigationUp = onNavigationUp,
        title = title,
        contentAlignment = if (isSelected) Alignment.CenterStart else Alignment.Center,
        /*content = {
            if (!isDeletable) {
                SearchView(
                    textState = textState,
                    onTextChange = onSearchTextChange,
                    trailingIcon = Icons.Default.Search,
                    bgColor = MyAppTheme.colors.lightBlue2,
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.top_bar_profile)),
                    paddingValues = PaddingValues(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = dimensionResource(id = R.dimen.padding),
                        end = 0.dp
                    )
                )
            } else {
                CustomText(
                    text = title,
                    style = MyAppTheme.typography.Semibold52_5,
                    color = MyAppTheme.colors.black
                )
            }

        },*/
        trailingContent = {

            if (isSelected) {
                if (isDeletable) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete_top),
                        contentDescription = "Delete",
                        tint = MyAppTheme.colors.black,
                        modifier = Modifier
                            .roundedCornerBackground(MyAppTheme.colors.transparent)
                            .size(25.dp)
                            .clickableWithNoRipple { onDeleteClick() }
                    )
                }
            } else {
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


        },
        modifier = modifier
    )
}

@SuppressLint("SimpleDateFormat")
@Composable
fun TransactionItem(
    item: MerchantDataWithAllData,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit,
    isSelected: Boolean = false,
    itemBgColor: Color,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val imageVector =
        if (isSelected) Icons.Default.Done else ImageVector.vectorResource(
            getCategoryIconById(
                item.categoryIconId,
                LocalContext.current
            )
        )
    val iconBgColor = if (isSelected) MyAppTheme.colors.brand else MyAppTheme.colors.lightBlue2
    //val amount = if (item.type > 0) item.amount else item.amount * -1
    val amountColor = if (item.type >= 0) MyAppTheme.colors.greenText else MyAppTheme.colors.redText

    ListItem(
        isClickable = true,
        onClick = onClick,
        onLongClick = onLongClick,
        leadingIcon = {
            RoundImage(
                imageVector = imageVector,
                imageVectorSize = 24.dp,
                //brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                tint = getCategoryColorById(item.categoryIconColorId),
                backGround = iconBgColor,
                contentDescription = "person",
                modifier = Modifier.size(50.dp)
            )
        },
        content = {
            Column {
                CustomText(
                    text = if (item.details?.trim()
                            ?.isNotEmpty() == true
                    ) item.details else item.categoryName,
                    style = MyAppTheme.typography.Semibold52_5,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!item.details.isNullOrEmpty()) {
                    CustomText(
                        text = item.merchantName ?: "",
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                /*CustomText(
                    text = Util.getFormattedStringWithSymbol(amount),
                    style = MyAppTheme.typography.Semibold52_5,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!item.details.isNullOrEmpty()) {
                    CustomText(
                        text = item.details,
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }*/
            }
        },
        trailingContent = {

            Column(
                horizontalAlignment = Alignment.End,
            ) {

                val format = SimpleDateFormat("dd MMM yyyy")

                val dayString = when (val day = getDateFromMillis(item.dateInMilli, format)) {
                    getTodayDate(format) -> stringResource(id = R.string.today)
                    getYesterdayDate(format) -> stringResource(id = R.string.yesterday)
                    else -> day
                }

                CustomText(
                    text = Util.getFormattedStringWithSymbol(item.originalAmount, item.originalAmountSymbol),
                    style = MyAppTheme.typography.Regular51,
                    color = amountColor,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(0.5f),
                    textAlign = TextAlign.Right,
                    maxLines = 1
                )
                CustomText(
                    text = dayString,
                    style = MyAppTheme.typography.Medium40,
                    color = MyAppTheme.colors.gray2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Right,
                    maxLines = 1
                )


            }
            /*Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                val format = SimpleDateFormat("dd MMM yyyy")

                val dayString = when (val day = Util.getDateFromMillis(item.dateInMilli, format)) {
                    Util.getTodayDate(format) -> stringResource(id = R.string.today)
                    Util.getYesterdayDate(format) -> stringResource(id = R.string.yesterday)
                    else -> day
                }
                Text(
                    text = dayString,
                    style = MyAppTheme.typography.Medium40,
                    color = MyAppTheme.colors.gray1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Right,
                    maxLines = 1
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.merchantName,
                        style = MyAppTheme.typography.Medium34,
                        color = MyAppTheme.colors.gray2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(0.5f),
                        textAlign = TextAlign.Right,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        painter = painterResource(getPaymentModeIcon(item.paymentName)),
                        contentDescription = "payment",
                        tint = MyAppTheme.colors.lightBlue2,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.small_icon_size))
                    )
                }

            }*/

        },
        isSetDivider = false,
        modifier = modifier.padding(vertical = 5.dp),
        isSelected = isSelected,
        itemBgColor = itemBgColor
    )
}

@Preview(showBackground = true)
@Composable
private fun AllDataTopBarPreview() {
    PennyPalTheme(darkTheme = true) {
        AllDataTopBar(
            textState = TextFieldState(),
            onAddClick = {},
            onDeleteClick = {},
            onNavigationUp = {},
            onSearchTextChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantListItemPreview() {
    PennyPalTheme(darkTheme = true) {

    }
}