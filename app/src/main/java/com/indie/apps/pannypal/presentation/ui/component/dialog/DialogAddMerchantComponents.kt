package com.indie.apps.pannypal.presentation.ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.DialogTextFieldItem


@Composable
fun AddMerchantDialogField()
{
    Column {
        DialogTextFieldItem(
            imageVector = Icons.Default.PersonOutline,
            placeholder = R.string.merchant_name_placeholder)
        DialogTextFieldItem(
            imageVector = Icons.Default.Phone,
            placeholder = R.string.phone_number_placeholder)
        DialogTextFieldItem(
            imageVector = Icons.Default.Details,
            placeholder = R.string.description_placeholder)
    }
}