package com.indie.apps.pennypal.presentation.ui.dialog.select_payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.payment.PaymentWithMode
import com.indie.apps.pennypal.presentation.ui.component.AccountTypeItem

@Composable
fun SelectPaymentDialogField(
    currentId: Long,
    paymentList: List<PaymentWithMode>,
    onSelectPayment: (PaymentWithMode) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
            .fillMaxWidth()
    ) {
        val bankList = paymentList.filter { item ->
            item.modeName != "Cash"
        }

        val cashList = paymentList.filter { item ->
            item.modeName == "Cash"
        }

        if(bankList.isNotEmpty()){

            AccountTypeItem(
                titleId = R.string.bank,
                isSelectable = true,
                isEditable = false,
                dataList = bankList,
                selectPaymentId = currentId,
                onSelect = {
                    onSelectPayment(it)
                }
            )
        }
        
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.item_inner_padding)))

        if(cashList.isNotEmpty()){
            AccountTypeItem(
                titleId = R.string.bank,
                isSelectable = true,
                isEditable = false,
                dataList = cashList,
                selectPaymentId = currentId,
                onSelect = {
                    onSelectPayment(it)
                }
            )
        }
    }
}