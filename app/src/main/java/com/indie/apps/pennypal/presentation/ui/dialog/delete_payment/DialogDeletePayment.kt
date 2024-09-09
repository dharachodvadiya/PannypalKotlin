package com.indie.apps.pennypal.presentation.ui.dialog.delete_payment

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.PaymentWithIdName
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogDeletePayment(
    deletePaymentViewModel : DeletePaymentViewModel = hiltViewModel(),
    paymentData: PaymentWithIdName,
    onNavigationUp: () -> Unit,
    onDeleteSuccess: (Long) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    deletePaymentViewModel.setDeletePaymentData(paymentData)

    val paymentState by deletePaymentViewModel.paymentState.collectAsStateWithLifecycle()
    val newPaymentId by deletePaymentViewModel.newPaymentId.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val paymentDeleteToast = stringResource(id = R.string.payment_delete_success_message)
    MyAppDialog(
        isBackEnable = true,
        title = R.string.delete_payment,
        onNavigationUp = {
            onNavigationUp()
        },
        content = {
            DeleteDialogField(
                deleteName = paymentData.name,
                currentId = newPaymentId,
                paymentList = paymentState,
                onSelectPayment = deletePaymentViewModel::selectPayment
            )
        },
        bottomContent = {
            BottomSaveButton(
                textId = R.string.confirm,
                onClick = {
                    deletePaymentViewModel.onDeleteDialogClick(
                        deleteId = paymentData.id,
                        onSuccess = {
                            context.showToast(paymentDeleteToast)
                            onDeleteSuccess(it)
                        }
                    )
                },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
            )
        },
        modifier = modifier,
        isFixHeight = true
    )
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
      /*  DialogDeletePayment(
            onNavigationUp = {},
            onDeleteSuccess = { },
        )*/
    }
}