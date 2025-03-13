package com.indie.apps.pennypal.presentation.ui.screen.new_item

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.presentation.ui.component.DialogSelectableItem
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.AutoSizeText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomTab
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.FlowRowItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppTextField
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getCategoryColorById
import com.indie.apps.pennypal.util.getCategoryIconById
import com.indie.apps.pennypal.util.getDateFromMillis
import com.indie.apps.pennypal.util.getPaymentModeIcon
import com.indie.apps.pennypal.util.getTimeFromMillis
import java.text.SimpleDateFormat

sealed interface NewEntryEvent {
    data object MerchantSelect : NewEntryEvent
    data object PaymentSelect : NewEntryEvent
    data class CategorySelect(val category: Category) : NewEntryEvent
    data object CurrencyChange : NewEntryEvent
    data object MoreCategories : NewEntryEvent
    data object DateSelect : NewEntryEvent
    data object TimeSelect : NewEntryEvent
    data object RetryRateFetch : NewEntryEvent
    data class AmountChange(val value: String) : NewEntryEvent
    data class RateChange(val value: String) : NewEntryEvent
    data class DescriptionChange(val value: String) : NewEntryEvent
}

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
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewEntryFieldItemSection(
    onEvent: (NewEntryEvent) -> Unit,
    merchantName: String? = null,
    paymentName: String? = null,
    category: Category? = null,
    merchantError: String = "",
    paymentError: String = "",
    categoryError: String = "",
    isMerchantLock: Boolean,
    currentTimeInMilli: Long,
    amount: TextFieldState = TextFieldState(),
    description: TextFieldState = TextFieldState(),
    rate: TextFieldState = TextFieldState(),
    finalAmount: Double,
    categories: List<Category>,
    focusRequesterAmount: FocusRequester,
    focusRequesterDescription: FocusRequester,
    currency: String,
    baseCurrency: String,
    isSameCurrency: Boolean,
    rateState: Resource<Unit>,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MyAppTheme.colors.transparent),
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        NewEntryDateTimeItem(
            currentTimeInMilli = currentTimeInMilli,
            onDateSelect = { onEvent(NewEntryEvent.DateSelect) },
            onTimeSelect = { onEvent(NewEntryEvent.TimeSelect) }
        )

        Spacer(modifier = Modifier.height(30.dp))

        DialogTextFieldItem(
            textState = amount,
            leadingIcon = {

                PrimaryButton(
                    modifier = Modifier
                        .width(40.dp)
                        .height(50.dp),
                    bgColor = MyAppTheme.colors.itemBg,
                    onClick = {
                        onEvent(NewEntryEvent.CurrencyChange)
                    }) {
                    AutoSizeText(
                        text = currency,
                        color = MyAppTheme.colors.black,
                        style = MyAppTheme.typography.Regular66_5
                    )
                }
            },
            placeholder = R.string.amount_placeholder,
            bgColor = MyAppTheme.colors.transparent,
            onTextChange = { onEvent(NewEntryEvent.AmountChange(it)) },
            isBottomLineEnable = true,
            keyboardType = KeyboardType.Number,
            focusRequester = focusRequesterAmount,
            nextFocusRequester = focusRequesterDescription
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
            onTextChange = { onEvent(NewEntryEvent.DescriptionChange(it)) },
            isBottomLineEnable = true,
            focusRequester = focusRequesterDescription
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
        ) {
            val maxCategoryItems = 5
            val isFirstItemAdded =
                category != null && categories.firstOrNull { it.id == category.id } == null
            val availableCategorySlots =
                if (isFirstItemAdded) maxCategoryItems - 1 else maxCategoryItems

            if (isFirstItemAdded) {
                FlowRowItem(
                    isSelected = true,
                    text = category!!.name, // Safe due to null check above
                    onClick = { onEvent(NewEntryEvent.CategorySelect(category)) }
                )
            }

            categories.take(availableCategorySlots).forEach { categoryItem ->
                FlowRowItem(
                    isSelected = category?.id == categoryItem.id,
                    text = categoryItem.name,
                    onClick = { onEvent(NewEntryEvent.CategorySelect(categoryItem)) }
                )
            }

            FlowRowItem(
                isSelected = false,
                text = stringResource(R.string.select_more),
                onClick = { onEvent(NewEntryEvent.MoreCategories) },
                bgColor = MyAppTheme.colors.gray3,
                textColor = MyAppTheme.colors.gray1
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        DialogSelectableItem(
            text = paymentName ?: "",
            label = R.string.payment_type,
            onClick = { onEvent(NewEntryEvent.PaymentSelect) },
            placeholder = R.string.add_payment_type_placeholder,
            isSelectable = true,
            errorText = paymentError,
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Add",
                    tint = MyAppTheme.colors.gray1
                )
            },
            leadingContent = {
                val icon = painterResource(getPaymentModeIcon(paymentName ?: ""))
                Icon(
                    painter = icon,
                    contentDescription = "",
                    tint = MyAppTheme.colors.gray1,
                )
            },
        )

        DialogSelectableItem(
            text = merchantName,
            label = R.string.merchant_optional,
            onClick = { onEvent(NewEntryEvent.MerchantSelect) },
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
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "",
                    tint = MyAppTheme.colors.gray1,
                )
            },
        )
        Spacer(Modifier.height(10.dp))

        if (!isSameCurrency) {

            val isReadOnly = remember { mutableStateOf(false) }
            val context = LocalContext.current

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.item_padding))
            ) {
                CustomText(
                    text = stringResource(R.string.final_amount) + " : ",
                    style = MyAppTheme.typography.Medium40,
                    color = MyAppTheme.colors.gray1,
                    modifier = Modifier.width(105.dp)
                )
                CustomText(
                    text = Util.getFormattedStringWithSymbol(finalAmount, baseCurrency),
                    style = MyAppTheme.typography.Medium46,
                    color = MyAppTheme.colors.black
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.item_padding))
            ) {
                CustomText(
                    text = stringResource(R.string.rate) + " : ",
                    style = MyAppTheme.typography.Medium40,
                    color = MyAppTheme.colors.gray1,
                    modifier = Modifier.width(100.dp)
                )
                MyAppTextField(
                    value = rate.text,
                    onValueChange = {
                        onEvent(NewEntryEvent.RateChange(it))
                    },
                    placeHolder = "1.0",
                    textStyle = MyAppTheme.typography.Medium46,
                    keyboardType = KeyboardType.Number,
                    placeHolderTextStyle = MyAppTheme.typography.Medium46,
                    modifier = Modifier
                        .height(25.dp)
                        .width(100.dp),
                    readOnly = isReadOnly.value,
                    bgColor = MyAppTheme.colors.brand.copy(alpha = 0.5f),
                    maxLines = 1,
                    textModifier = Modifier.padding(horizontal = dimensionResource(R.dimen.item_padding))
                )

                when (rateState) {
                    is Resource.Error -> {
                        context.showToast(rateState.message ?: "")
                        isReadOnly.value = false
                        Icon(
                            imageVector = Icons.Default.Refresh, // Retry icon
                            contentDescription = "Retry",
                            tint = MyAppTheme.colors.redBg,
                            modifier = Modifier
                                .size(20.dp)
                                .clickableWithNoRipple { onEvent(NewEntryEvent.RetryRateFetch) }
                                .padding(2.dp)
                        )
                    }

                    is Resource.Loading -> {
                        isReadOnly.value = true
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    is Resource.Success -> {
                        isReadOnly.value = false
                    }
                }
            }
        }
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
            .clickableWithNoRipple {
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