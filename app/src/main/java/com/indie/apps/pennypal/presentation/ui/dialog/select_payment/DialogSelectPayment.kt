package com.indie.apps.pennypal.presentation.ui.dialog.select_payment

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.module.toPayment
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogSelectPayment(
    selectPaymentViewModel: SelectPaymentViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    selectedId: Long,
    onSelect: (Payment) -> Unit,
    onSaveSuccess: () -> Unit,
    isSavable: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val paymentState by selectPaymentViewModel.paymentState.collectAsStateWithLifecycle()
    var currentId by remember {
        mutableLongStateOf(selectedId)
    }

    //val paymentState = emptyList<PaymentWithMode>()
    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_payment,
        onNavigationUp = {
            onNavigationUp()
        },
        content = {
            SelectPaymentDialogField(
                currentId = currentId,
                paymentList = paymentState,
                onSelectPayment = {
                    if(!isSavable) {
                        onSelect(it.toPayment())
                    }else{
                        currentId = it.id
                    }
                }
            )
        },
        bottomContent = {
            if(isSavable){
                BottomSaveButton(
                    onClick = {
                        selectPaymentViewModel.saveDefaultPayment(currentId){
                            onSaveSuccess()
                        }
                    },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
                )
            }
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
            selectedId = 1L,
            onSaveSuccess = {}
        )
    }
}