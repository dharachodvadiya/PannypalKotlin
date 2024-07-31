package com.indie.apps.pannypal.presentation.ui.component.dialog

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState


@Composable
fun AddMerchantDialogField(
    nameState: TextFieldState,
    phoneNoState: TextFieldState,
    descState: TextFieldState
)
{
    Column {
        DialogTextFieldItem(
            textState = nameState,
            imageVector = Icons.Default.PersonOutline,
            placeholder = R.string.merchant_name_placeholder)
        DialogTextFieldItem(
            textState = phoneNoState,
            imageVector = Icons.Default.Phone,
            placeholder = R.string.phone_number_placeholder,
            keyboardType = KeyboardType.Number)
        DialogTextFieldItem(
            textState = descState,
            imageVector = Icons.Default.Details,
            placeholder = R.string.description_placeholder)
    }
}