package com.indie.apps.pennypal.presentation.ui.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.component.dialog.AddPaymentDialogField
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.presentation.viewmodel.AddPaymentViewModel

@Composable
fun DialogAddPayment(
    addPaymentViewModel: AddPaymentViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSaveSuccess: (Payment?) -> Unit,
    modifier: Modifier = Modifier
) {
    val enableButton by addPaymentViewModel.enableButton.collectAsStateWithLifecycle()
    val paymentTypeState by addPaymentViewModel.paymentTypeState.collectAsStateWithLifecycle()
    MyAppDialog(
        title = R.string.add_payment,
        onNavigationUp = {
            if (enableButton)
                onNavigationUp()
        },
        content = {
            AddPaymentDialogField(
                textPaymentState = paymentTypeState
            )
        },
        bottomContent = {
            BottomSaveButton(
                onClick = {
                    addPaymentViewModel.addPayment(onSuccess = onSaveSuccess)
                },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogAddPayment(
            onNavigationUp = {},
            onSaveSuccess = {}
        )
    }
}