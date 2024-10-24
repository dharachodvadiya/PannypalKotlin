package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_merchant

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme


@Composable
fun AddMerchantDialogField(
    nameState: TextFieldState,
    phoneNoState: TextFieldState,
    descState: TextFieldState,
    onCpp: () -> Unit,
    onContactBook: () -> Unit,
    countryCode: String = "+00",
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.padding)
        )
    ) {
        DialogTextFieldItem(
            textState = nameState,
            imageVector = Icons.Default.PersonOutline,
            placeholder = R.string.merchant_name_placeholder,
            textTrailingContent = {
                PrimaryButton(
                    bgColor = MyAppTheme.colors.white,
                    borderStroke = BorderStroke(
                        width = 1.dp,
                        color = MyAppTheme.colors.gray1
                    ),
                    onClick = onContactBook,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.phone_book),
                        contentDescription = "Add",
                        tint = MyAppTheme.colors.gray1
                    )
                }
            }
        )
        DialogTextFieldItem(
            textState = phoneNoState,
            imageVector = Icons.Default.Phone,
            placeholder = R.string.phone_number_placeholder,
            textLeadingContent = {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .clickable { onCpp() }
                ) {
                    CustomText(text = countryCode)
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }

            },
            keyboardType = KeyboardType.Number
        )
        DialogTextFieldItem(
            textState = descState,
            imageVector = Icons.Default.Details,
            placeholder = R.string.description_placeholder
        )
    }
}

@Preview
@Composable
private fun AddMerchantDialogFieldPreview() {
    PennyPalTheme(darkTheme = true) {
        AddMerchantDialogField(
            nameState = TextFieldState(),
            phoneNoState = TextFieldState(),
            descState = TextFieldState(),
            onCpp = {},
            onContactBook = {}
        )
    }
}