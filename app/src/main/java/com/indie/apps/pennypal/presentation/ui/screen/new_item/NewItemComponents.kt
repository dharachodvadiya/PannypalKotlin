package com.indie.apps.pennypal.presentation.ui.screen.new_item

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.presentation.ui.component.DialogSelectableItem
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomTab
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.FlowRowItem
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getCategoryColorById
import com.indie.apps.pennypal.util.getCategoryIconById
import com.indie.apps.pennypal.util.getDateFromMillis
import com.indie.apps.pennypal.util.getTimeFromMillis
import java.text.SimpleDateFormat

@Composable
fun NewEntryTopSelectionButton(
    received: Boolean,
    onReceivedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val list = listOf(
        TabItemInfo(
            title = R.string.received,
            icon = Icons.Default.SouthWest,
            selectBgColor = MyAppTheme.colors.greenBg,
            unSelectBgColor = MyAppTheme.colors.itemBg,
            selectContentColor = MyAppTheme.colors.black,
            unSelectContentColor = MyAppTheme.colors.gray1
        ),
        TabItemInfo(
            title = R.string.spent,
            icon = Icons.Default.NorthEast,
            selectBgColor = MyAppTheme.colors.redBg,
            unSelectBgColor = MyAppTheme.colors.itemBg,
            selectContentColor = MyAppTheme.colors.black,
            unSelectContentColor = MyAppTheme.colors.gray1
        )
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
    ) {
        CustomTab(
            tabList = list,
            selectedIndex = if (received) 0 else 1,
            onTabSelected = {
                onReceivedChange(it == 0)
            })
        /*NewEntryButtonItem(
            text = R.string.received,
            onClick = {
                onReceivedChange(true)
            },
            bgColor = MyAppTheme.colors.greenBg,
            imageVector = Icons.Default.SouthWest,
            modifier = Modifier.weight(0.47f),
            selected = received
        )
        Spacer(modifier = Modifier.weight(0.06f))
        NewEntryButtonItem(
            text = R.string.spent,
            onClick = {
                onReceivedChange(false)
            },
            bgColor = MyAppTheme.colors.redBg,
            imageVector = Icons.Default.NorthEast,
            modifier = Modifier.weight(0.47f),
            selected = !received
        )*/
    }
}
/*
@Composable
private fun NewEntryButtonItem(
    @StringRes text: Int,
    onClick: () -> Unit,
    bgColor: Color,
    imageVector: ImageVector,
    selected: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    val btnBgColor = if (selected) bgColor else MyAppTheme.colors.itemBg
    val btnContentColor = if (selected) MyAppTheme.colors.black else MyAppTheme.colors.gray1
    PrimaryButton(
        bgColor = btnBgColor,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "",
                tint = btnContentColor
            )
            Spacer(modifier = Modifier.width(5.dp))
            CustomText(
                text = stringResource(text),
                style = MyAppTheme.typography.Medium45_29,
                color = btnContentColor
            )
        }
    }
}*/

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewEntryFieldItemSection(
    onMerchantSelect: () -> Unit,
    onPaymentSelect: () -> Unit,
    onSelectMoreCategory: () -> Unit,
    onSelectCategory: (Category) -> Unit,
    merchantName: String? = null,
    paymentName: String? = null,
    category: Category? = null,
    merchantError: String = "",
    paymentError: String = "",
    categoryError: String = "",
    isMerchantLock: Boolean,
    currentTimeInMilli: Long,
    onDateSelect: () -> Unit,
    onTimeSelect: () -> Unit,
    amount: TextFieldState = TextFieldState(),
    description: TextFieldState = TextFieldState(),
    onAmountTextChange: (String) -> Unit,
    onDescTextChange: (String) -> Unit,
    categories: List<Category>,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MyAppTheme.colors.transparent),
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        NewEntryDateTimeItem(
            currentTimeInMilli = currentTimeInMilli,
            onDateSelect = onDateSelect,
            onTimeSelect = onTimeSelect
        )

        Spacer(modifier = Modifier.height(30.dp))

        /*NewEntryTextFieldItem(
            label = R.string.description,
            placeholder = R.string.description_placeholder,
            textState = description,
            onTextChange = onDescTextChange
        )
        TextFieldError(
            textError = description.errorText.asString()
        )*/

        DialogTextFieldItem(
            textState = amount,
            leadingIcon = {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomText(
                        text = Util.currentCurrencySymbol,
                        color = MyAppTheme.colors.black,
                        style = MyAppTheme.typography.Regular66_5
                    )
                }
            },
            placeholder = R.string.amount_placeholder,
            bgColor = MyAppTheme.colors.transparent,
            onTextChange = onAmountTextChange,
            isBottomLineEnable = true,
            keyboardType = KeyboardType.Number
        )

        DialogTextFieldItem(
            textState = description,
            leadingIcon = {
                val icon = ImageVector.vectorResource(
                    getCategoryIconById(
                        category?.iconId ?: 0,
                        LocalContext.current
                    )
                )
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = getCategoryColorById(category?.iconColorId ?: 0),
                )
            },
            placeholder = R.string.description_placeholder,
            bgColor = MyAppTheme.colors.transparent,
            onTextChange = onDescTextChange,
            isBottomLineEnable = true
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
        ) {

            categories.onEach {
                FlowRowItem(
                    isSelected = category?.id == it.id,
                    text = it.name,
                    onClick = { onSelectCategory(it) })
            }

            FlowRowItem(
                isSelected = false,
                text = stringResource(R.string.select_more),
                onClick = {
                    onSelectMoreCategory()
                },
                bgColor = MyAppTheme.colors.gray3,
                textColor = MyAppTheme.colors.gray1
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        /*Spacer(modifier = Modifier.height(10.dp))

        NewEntryTextFieldItem(
            label = R.string.amount,
            placeholder = R.string.amount_placeholder,
            textState = amount,
            keyboardType = KeyboardType.Number,
            onTextChange = onAmountTextChange
        )
        TextFieldError(
            textError = amount.errorText.asString()
        )*/

        DialogSelectableItem(
            text = paymentName ?: "",
            label = R.string.payment_type,
            onClick = onPaymentSelect,
            placeholder = R.string.add_payment_type_placeholder,
            isSelectable = true,
            errorText = paymentError,
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Add",
                    tint = MyAppTheme.colors.gray1
                )
            }
        )

        DialogSelectableItem(
            text = merchantName,
            label = R.string.merchant_optional,
            onClick = onMerchantSelect,
            placeholder = R.string.add_merchant_placeholder,
            isSelectable = !isMerchantLock,
            errorText = merchantError,
            trailingContent = {
                if (!isMerchantLock) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Add",
                        tint = MyAppTheme.colors.gray1,
                    )
                }
            }
        )
        /*
                DialogSelectableItem(
                    text = category?.name ?: "",
                    label = R.string.category,
                    onClick = onCategorySelect,
                    placeholder = R.string.select_category_placeholder,
                    isSelectable = true,
                    errorText = categoryError,
                    trailingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Add",
                            tint = MyAppTheme.colors.gray1
                        )
                    }
                )*/
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun NewEntryDateTimeItem(
    currentTimeInMilli: Long,
    onDateSelect: () -> Unit,
    onTimeSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy")
    val timeFormat = SimpleDateFormat("hh:mm aa")
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
    ) {
        DateTimeSelectableItem(
            imageVector = Icons.Default.CalendarMonth,
            text = getDateFromMillis(currentTimeInMilli, dateFormat),
            onSelect = onDateSelect
        )

        DateTimeSelectableItem(
            imageVector = Icons.Default.AccessTime,
            text = getTimeFromMillis(currentTimeInMilli, timeFormat),
            onSelect = onTimeSelect
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun DateTimeSelectableItem(
    imageVector: ImageVector,
    text: String,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.item_inner_padding))
            .roundedCornerBackground(MyAppTheme.colors.transparent)
            .clickableWithNoRipple(
                //    interactionSource = MutableInteractionSource(),
                //   indication = null
            ) {
                onSelect()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            tint = MyAppTheme.colors.gray1,
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding)))

        CustomText(
            text = text,
            style = MyAppTheme.typography.Medium46,
            color = MyAppTheme.colors.black
        )
    }
}

/*@Composable
fun NewEntrySelectableItem(
    text: String? = null,
    @StringRes label: Int,
    @StringRes placeholder: Int,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isSelectable: Boolean,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        CustomText(
            text = stringResource(id = label),
            style = MyAppTheme.typography.Medium46,
            color = MyAppTheme.colors.gray1
        )
        Row(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .roundedCornerBackground(MyAppTheme.colors.itemBg)
                .clickable(enabled = isSelectable) { onClick() }
                .height(dimensionResource(id = R.dimen.new_entry_field_height))
                *//*.background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )*//*
                .padding(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = dimensionResource(id = R.dimen.padding),
                    end = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (text.isNullOrEmpty()) {
                CustomText(
                    text = stringResource(id = placeholder),
                    modifier = Modifier
                        .weight(1f),
                    style = MyAppTheme.typography.Regular46,
                    color = MyAppTheme.colors.gray2
                )
            } else {
                CustomText(
                    text = text,
                    modifier = Modifier
                        .weight(1f),
                    style = MyAppTheme.typography.Medium46,
                    color = MyAppTheme.colors.black
                )
            }

            if (trailingContent != null) {
                Spacer(modifier = Modifier.weight(1f))
                trailingContent()
            }


        }
    }

}*/
/*
@Composable
private fun NewEntryTextFieldItem(
    textState: TextFieldState = TextFieldState(),
    onTextChange: (String) -> Unit,
    @StringRes label: Int,
    placeholder: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val colorDivider = MyAppTheme.colors.gray1

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .drawBehind {
                drawLine(
                    colorDivider,
                    Offset(0f, size.height),
                    Offset(size.width, size.height),
                    2f
                )
            }
    ) {
        CustomText(
            text = stringResource(id = label),
            style = MyAppTheme.typography.Medium46,
            color = MyAppTheme.colors.gray1
        )
        MyAppTextField(
            value = textState.text,
            *//*onValueChange = {
                textState.disableError()
                textState.text = it
            },*//*
            onValueChange = onTextChange,
            placeHolder = stringResource(placeholder),
            textStyle = MyAppTheme.typography.Medium46,
            keyboardType = keyboardType,
            placeHolderTextStyle = MyAppTheme.typography.Regular46,
            modifier = Modifier.height(dimensionResource(id = R.dimen.new_entry_field_height)),
        )
    }
}*/

@Preview(showBackground = true)
@Composable
private fun TNewEntryFieldItemSectionPreview() {
    PennyPalTheme(darkTheme = true) {
        NewEntryFieldItemSection(
            onMerchantSelect = {},
            onPaymentSelect = {},
            onSelectMoreCategory = {},
            isMerchantLock = false,
            onDateSelect = {},
            onTimeSelect = {},
            currentTimeInMilli = 0L,
            onAmountTextChange = {},
            onDescTextChange = {},
            categories = emptyList(),
            onSelectCategory = {}
        )
    }
}
