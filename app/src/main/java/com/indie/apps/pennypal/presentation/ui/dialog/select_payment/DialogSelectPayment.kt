package com.indie.apps.pennypal.presentation.ui.dialog.select_payment

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.PaymentWithIdName
import com.indie.apps.pennypal.data.module.PaymentWithMode
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogSelectPayment(
    selectPaymentViewModel : SelectPaymentViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSelect: (Long) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val paymentState by selectPaymentViewModel.paymentState.collectAsStateWithLifecycle()

    //val paymentState = emptyList<PaymentWithMode>()
    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_payment,
        onNavigationUp = {
            onNavigationUp()
        },
        content = {
            SelectDialogField(
                currentId = 1L,
                paymentList = paymentState,
                onSelectPayment = onSelect
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
        DialogSelectPayment(
            onNavigationUp = {},
            onSelect = { },
        )
    }
}