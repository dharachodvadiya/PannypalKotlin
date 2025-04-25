package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_merchant

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
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
    onNameTextChange: (String) -> Unit,
    onPhoneNoTextChange: (String) -> Unit,
    onDescTextChange: (String) -> Unit,
    onCpp: () -> Unit,
    onContactBook: () -> Unit,
    countryCode: String = "+00",
    focusRequesterName: FocusRequester,
    focusRequesterPhoneNumber: FocusRequester,
    focusRequesterDescription: FocusRequester,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.padding)
        )
    ) {
        DialogTextFieldItem(
            textState = nameState,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PersonOutline,
                    contentDescription = "",
                    tint = MyAppTheme.colors.gray1
                )
            },
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
            },
            onTextChange = onNameTextChange,
            focusRequester = focusRequesterName,
            nextFocusRequester = focusRequesterPhoneNumber
        )
        DialogTextFieldItem(
            textState = phoneNoState, leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "",
                    tint = MyAppTheme.colors.gray1
                )
            },
            placeholder = R.string.phone_number_placeholder,
            textLeadingContent = {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .roundedCornerBackground(MyAppTheme.colors.transparent)
                        .clickableWithNoRipple { onCpp() }
                ) {
                    CustomText(text = countryCode)
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }

            },
            keyboardType = KeyboardType.Number,
            onTextChange = onPhoneNoTextChange,
            focusRequester = focusRequesterPhoneNumber,
            nextFocusRequester = focusRequesterDescription
        )
        DialogTextFieldItem(
            textState = descState, leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Details,
                    contentDescription = "",
                    tint = MyAppTheme.colors.gray1
                )
            },
            placeholder = R.string.description_placeholder,
            onTextChange = onDescTextChange,
            focusRequester = focusRequesterDescription
        )
    }
}

@Preview
@Composable
private fun AddMerchantDialogFieldPreview() {
    PennyPalTheme(darkTheme = true) {
        /* AddMerchantDialogField(
             nameState = TextFieldState(),
             phoneNoState = TextFieldState(),
             descState = TextFieldState(),
             onCpp = {},
             onContactBook = {},
             onDescTextChange = {},
             onPhoneNoTextChange = {},
             onNameTextChange = {}
         )*/
    }
}