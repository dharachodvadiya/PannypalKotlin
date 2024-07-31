package com.indie.apps.pannypal.presentation.ui.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.component.dialog.AddPaymentDialogField
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.AddPaymentViewModel
import com.indie.apps.pannypal.util.ErrorMessage

@Composable
fun DialogAddPayment(
    addPaymentViewModel: AddPaymentViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSaveSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    // val paymentType by addPaymentViewModel.paymentType.collectAsStateWithLifecycle()

    val paymentTypeState by remember { mutableStateOf(TextFieldState()) }
    MyAppDialog(
        title = R.string.add_payment,
        onNavigationUp = onNavigationUp,
        content = {
            AddPaymentDialogField(
                textPaymentState = paymentTypeState
            )
        },
        bottomContent = {
            BottomSaveButton(
                onClick = {
                    addPaymentViewModel.addPayment(
                        payment = Payment(name = paymentTypeState.text.trim()),
                        onSuccess = onSaveSuccess,
                        onFail = {
                            paymentTypeState.setError(ErrorMessage.PAYMENT_TYPE_EXIST)
                        }

                    )
                },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
            )
        },
        modifier = modifier
    )
}

@Preview()
@Composable
private fun MyAppDialogPreview() {
    PannyPalTheme {
        DialogAddPayment(
            onNavigationUp = {},
            onSaveSuccess = {}
        )
    }
}