package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_payment

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogAddPayment(
    addPaymentViewModel: AddEditPaymentViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSaveSuccess: (Payment?, Boolean) -> Unit,
    editId: Long? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val enableButton by addPaymentViewModel.enableButton.collectAsStateWithLifecycle()
    val paymentTypeState by addPaymentViewModel.paymentTypeState.collectAsStateWithLifecycle()

    val paymentModeList by addPaymentViewModel.paymentModeState.collectAsStateWithLifecycle()
    val selectedModeId by addPaymentViewModel.selectedModeId.collectAsStateWithLifecycle()

    LaunchedEffect(editId) {
        addPaymentViewModel.setEditId(editId) // always call after set country code
    }

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
    MyAppDialog(
        title = R.string.add_payment,
        onNavigationUp = {
            if (enableButton)
                onNavigationUp()
        },
        content = {
            AddPaymentDialogField(
                textPaymentState = paymentTypeState,
                paymentModeList = paymentModeList,
                currentModId = selectedModeId,
                onModeChange = addPaymentViewModel::onModeChange
            )
        },
        bottomContent = {
            BottomSaveButton(
                onClick = {
                    addPaymentViewModel.addEditPayment(onSuccess = onSaveSuccess)
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