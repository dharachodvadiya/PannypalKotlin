package com.indie.apps.pannypal.presentation.ui.component.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState


@Composable
fun AddPaymentDialogField(
    textPaymentState: TextFieldState
) {
    DialogTextFieldItem(
        textState = textPaymentState,
        imageVector = Icons.Default.Payment,
        placeholder = R.string.add_payment_type_placeholder
    )
    Spacer(modifier = Modifier.fillMaxHeight(0.1f))
}

