package com.indie.apps.pennypal.presentation.ui.screen.new_item

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.TextFieldError
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppTextField
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun NewEntryTopSelectionButton(
    received: Boolean,
    onReceivedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
    ) {
        NewEntryButtonItem(
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
        )
    }
}

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
}

@Composable
fun NewEntryFieldItemSection(
    onMerchantSelect: () -> Unit,
    onPaymentSelect: () -> Unit,
    onCategorySelect: () -> Unit,
    merchantName: String? = null,
    paymentName: String? = null,
    categoryName: String? = null,
    merchantError: String = "",
    paymentError: String = "",
    categoryError: String = "",
    isMerchantLock: Boolean,
    amount: TextFieldState = TextFieldState(),
    description: TextFieldState = TextFieldState(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MyAppTheme.colors.transparent),
    ) {
        /*NewEntrySelectableItem(
            text = merchantName,
            label = R.string.merchant,
            imageVector = Icons.Default.PersonAddAlt1,
            onAddClick = onMerchantSelect,
            placeholder = R.string.add_merchant_placeholder,
            isSelectable = !isMerchantLock
        )*/

        NewEntrySelectableItem(
            text = merchantName,
            label = R.string.merchant,
            onClick = onMerchantSelect,
            placeholder = R.string.add_merchant_placeholder,
            isSelectable = !isMerchantLock,
            trailingContent = {
                if (!isMerchantLock) {
                    PrimaryButton(
                        bgColor = MyAppTheme.colors.white,
                        borderStroke = BorderStroke(
                            width = 1.dp,
                            color = MyAppTheme.colors.gray1
                        ),
                        onClick = onMerchantSelect,
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAddAlt1,
                            contentDescription = "Add",
                            tint = MyAppTheme.colors.gray1
                        )
                    }
                }
            }
        )
        TextFieldError(
            textError = merchantError
        )
        Spacer(modifier = Modifier.height(10.dp))

        NewEntrySelectableItem(
            text = paymentName ?: "",
            label = R.string.payment_type,
            onClick = onPaymentSelect,
            placeholder = R.string.add_payment_type_placeholder,
            isSelectable = true,
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Add",
                    tint = MyAppTheme.colors.gray1,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.item_inner_padding))
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .clickable {
                            onPaymentSelect()
                        }
                )
            }
        )
        TextFieldError(
            textError = paymentError
        )

        NewEntrySelectableItem(
            text = categoryName ?: "",
            label = R.string.category,
            onClick = onCategorySelect,
            placeholder = R.string.add_category_placeholder,
            isSelectable = true,
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Add",
                    tint = MyAppTheme.colors.gray1,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.item_inner_padding))
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .clickable {
                            onCategorySelect()
                        }
                )
            }
        )
        TextFieldError(
            textError = categoryError
        )

        //Spacer(modifier = Modifier.height(20.dp))
        NewEntryTextFieldItem(
            label = R.string.amount,
            placeholder = R.string.amount_placeholder,
            textState = amount,
            keyboardType = KeyboardType.Number
        )
        TextFieldError(
            textError = amount.errorText
        )
        Spacer(modifier = Modifier.height(10.dp))


        NewEntryTextFieldItem(
            label = R.string.description,
            placeholder = R.string.description_placeholder,
            textState = description
        )
        TextFieldError(
            textError = description.errorText
        )

    }
}

@Composable
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
                /*.background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )*/
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

}

@Composable
private fun NewEntryTextFieldItem(
    textState: TextFieldState = TextFieldState(),
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
            onValueChange = {
                textState.disableError()
                textState.text = it
            },
            placeHolder = stringResource(placeholder),
            textStyle = MyAppTheme.typography.Medium46,
            keyboardType = keyboardType,
            placeHolderTextStyle = MyAppTheme.typography.Regular46,
            placeHolderColor = MyAppTheme.colors.gray2,
            modifier = Modifier.height(dimensionResource(id = R.dimen.new_entry_field_height)),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TNewEntryFieldItemSectionPreview() {
    PennyPalTheme(darkTheme = true) {
        NewEntryFieldItemSection(
            onMerchantSelect = {},
            onPaymentSelect = {},
            onCategorySelect = {},
            isMerchantLock = false
        )
    }
}
