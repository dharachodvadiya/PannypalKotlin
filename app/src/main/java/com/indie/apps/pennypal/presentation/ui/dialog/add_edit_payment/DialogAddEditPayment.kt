package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_payment

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
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogAddPayment(
    addPaymentViewModel: AddEditPaymentViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSaveSuccess: (Payment?, Boolean) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val enableButton by addPaymentViewModel.enableButton.collectAsStateWithLifecycle()
    val paymentTypeState by addPaymentViewModel.paymentTypeState.collectAsStateWithLifecycle()

    val paymentModeList by addPaymentViewModel.paymentModeState.collectAsStateWithLifecycle()
    val selectedModeId by addPaymentViewModel.selectedModeId.collectAsStateWithLifecycle()

    /*var showAnimatedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showAnimatedDialog = true
    }*/

    /*val scope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = showAnimatedDialog,
        enter = slideInVertically { fullHeight -> (fullHeight + fullHeight / 2) } +
                expandVertically(
                    // Expand from the top.
                    expandFrom = Alignment.Top
                ) +
                fadeIn(
                    initialAlpha = 0.5f
                ),
        exit = slideOutVertically { fullHeight -> (fullHeight + fullHeight / 2) } + fadeOut()
    ) {*/
    val context = LocalContext.current
    val paymentSaveToast = stringResource(id = R.string.payment_save_success_toast)
    val paymentEditToast = stringResource(id = R.string.payment_edit_success_toast)

    MyAppDialog(
        title = if (!addPaymentViewModel.getIsEditable()) R.string.add_payment else R.string.edit_payment,
        onNavigationUp = {
            if (enableButton)
                onNavigationUp()
        },
        content = {
            AddPaymentDialogField(
                textPaymentState = paymentTypeState,
                paymentModeList = paymentModeList,
                currentModId = selectedModeId,
                onModeChange = addPaymentViewModel::onModeChange,
                onPaymentTypeTextChange = addPaymentViewModel::updatePaymentTypeText
            )
        },
        bottomContent = {
            BottomSaveButton(
                onClick = {
                    addPaymentViewModel.addEditPayment { payment, isEdit ->
                        onSaveSuccess(payment, isEdit)
                        context.showToast(if (isEdit) paymentEditToast else paymentSaveToast)
                    }
                },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
            )
        },
        modifier = modifier
    )
    //}
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogAddPayment(
            onNavigationUp = {},
            onSaveSuccess = { _, _ -> }
        )
    }
}