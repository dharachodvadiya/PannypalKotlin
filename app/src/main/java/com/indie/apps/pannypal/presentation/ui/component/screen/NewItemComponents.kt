package com.indie.apps.pannypal.presentation.ui.component.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.presentation.ui.component.TextFieldError
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppTextField
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

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
    modifier: Modifier = Modifier,
) {
    val btnBgColor = if (selected) bgColor else MyAppTheme.colors.gray0
    val btnContentColor = if (selected) MyAppTheme.colors.white else MyAppTheme.colors.gray1
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
            Text(
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
    onPaymentAdd: () -> Unit,
    paymentList: List<Payment>,
    onPaymentSelect: (Payment) -> Unit,
    merchantName: String? = null,
    paymentName: String? = null,
    merchantError: String = "",
    paymentError: String = "",
    amount: TextFieldState = TextFieldState(),
    description: TextFieldState = TextFieldState(),
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MyAppTheme.colors.white),
    ) {
        NewEntrySelectableItem(
            text = merchantName,
            label = R.string.merchant,
            imageVector = Icons.Default.PersonAddAlt1,
            onAddClick = onMerchantSelect,
            placeholder = R.string.add_merchant_placeholder
        )
        TextFieldError(
            textError = merchantError
        )
        Spacer(modifier = Modifier.height(10.dp))
        NewEntryDropDownList(
            label = R.string.payment_type,
            placeholder = R.string.add_payment_type_placeholder,
            selectedValue = paymentName ?: "",
            onValueChangedEvent = onPaymentSelect,
            onAddClick = onPaymentAdd,
            options = paymentList
        )
        TextFieldError(
            textError = paymentError
        )

        Spacer(modifier = Modifier.height(20.dp))
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
private fun NewEntrySelectableItem(
    text: String? = null,
    @StringRes label: Int,
    @StringRes placeholder: Int,
    imageVector: ImageVector,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = label),
            style = MyAppTheme.typography.Medium46,
            color = MyAppTheme.colors.gray2
        )
        Row(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .clickable { onAddClick() }
                .height(dimensionResource(id = R.dimen.new_entry_field_hight))
                .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.gray0
                )
                .padding(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = dimensionResource(id = R.dimen.padding),
                    end = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (text.isNullOrEmpty()) {
                Text(
                    text = stringResource(id = placeholder),
                    modifier = Modifier
                        .weight(1f),
                    style = MyAppTheme.typography.Regular46,
                    color = MyAppTheme.colors.gray2
                )
            } else {
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f),
                    style = MyAppTheme.typography.Medium46,
                    color = MyAppTheme.colors.black
                )
            }
            PrimaryButton(
                bgColor = MyAppTheme.colors.white,
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = MyAppTheme.colors.gray2
                ),
                modifier = modifier,
                onClick = onAddClick,
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "Add",
                    tint = MyAppTheme.colors.gray2
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewEntryDropDownList(
    @StringRes label: Int,
    @StringRes placeholder: Int,
    selectedValue: String,
    options: List<Payment>,
    onAddClick: () -> Unit,
    onValueChangedEvent: (Payment) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value },
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = label),
                style = MyAppTheme.typography.Medium46,
                color = MyAppTheme.colors.gray2
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .clickable { expanded.value = !expanded.value }
                    .height(dimensionResource(id = R.dimen.new_entry_field_hight))
                    .background(
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                        color = MyAppTheme.colors.gray0
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyAppTextField(
                    bgColor = MyAppTheme.colors.transparent,
                    value = selectedValue,
                    onValueChange = {},
                    readOnly = true,
                    placeHolder = stringResource(placeholder),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                    },
                    textStyle = MyAppTheme.typography.Medium46,
                    placeHolderTextStyle = MyAppTheme.typography.Regular46,
                    modifier = Modifier.weight(1f),
                    textModifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    paddingValues = PaddingValues(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = dimensionResource(id = R.dimen.padding),
                        end = 4.dp
                    ),
                )

                PrimaryButton(
                    bgColor = MyAppTheme.colors.white,
                    borderStroke = BorderStroke(
                        width = 1.dp,
                        color = MyAppTheme.colors.gray2
                    ),
                    onClick = {
                        onAddClick()
                        expanded.value = false
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = MyAppTheme.colors.gray2
                    )
                }
            }
        }


        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }) {
            options.forEach { item: Payment ->
                DropdownMenuItem(
                    text = { Text(text = item.name) },
                    onClick = {
                        expanded.value = false
                        onValueChangedEvent(item)
                    }
                )
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
    modifier: Modifier = Modifier
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
        Text(
            text = stringResource(id = label),
            style = MyAppTheme.typography.Medium46,
            color = MyAppTheme.colors.gray2
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
            modifier = Modifier.height(dimensionResource(id = R.dimen.new_entry_field_hight))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TNewEntryFieldItemSectionPreview() {
    PannyPalTheme {
        NewEntryFieldItemSection(
            onMerchantSelect = {},
            onPaymentAdd = {},
            paymentList = emptyList(),
            onPaymentSelect = {}
        )
    }
}
