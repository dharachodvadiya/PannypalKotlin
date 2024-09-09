package com.indie.apps.pennypal.presentation.ui.dialog.select_payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.PaymentWithMode
import com.indie.apps.pennypal.presentation.ui.component.AccountItem
import com.indie.apps.pennypal.presentation.ui.component.AccountTypeItem
import com.indie.apps.pennypal.presentation.ui.screen.payment.AccountBankItem
import com.indie.apps.pennypal.presentation.ui.screen.payment.AccountCashItem
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun SelectDialogField(
    currentId: Long,
    paymentList: List<PaymentWithMode>,
    onSelectPayment: (Long) -> Unit
) {

    

    Column(
        modifier = Modifier
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
                isEditMode = true,
                isEditable = false,
                dataList = bankList,
                defaultPaymentId = 1L,
                onSelect = {
                    onSelectPayment(it)
                }
            )
        }
        
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.item_inner_padding)))

        if(cashList.isNotEmpty()){
            AccountTypeItem(
                titleId = R.string.bank,
                isEditMode = true,
                isEditable = false,
                dataList = cashList,
                defaultPaymentId = 1L,
                onSelect = {
                    onSelectPayment(it)
                }
            )
        }
    }
}