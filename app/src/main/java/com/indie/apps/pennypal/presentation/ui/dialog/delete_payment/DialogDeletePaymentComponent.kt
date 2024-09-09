package com.indie.apps.pennypal.presentation.ui.dialog.delete_payment

import androidx.compose.foundation.gestures.scrollable
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
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.module.PaymentWithMode
import com.indie.apps.pennypal.presentation.ui.component.AccountItem
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun DeleteDialogField(
    currentId: Long,
    paymentList: List<PaymentWithMode>,
    onSelectPayment: (Long) -> Unit,
    deleteName: String,
) {

    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.delete_payment_text1) + " $deleteName",
            style = MyAppTheme.typography.Bold49_5,
            color = MyAppTheme.colors.gray0
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.item_inner_padding)))

        Text(
            text = stringResource(id = R.string.delete_payment_text2),
            style = MyAppTheme.typography.Regular46,
            color = MyAppTheme.colors.gray1
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.item_inner_padding)))
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {

            paymentList.forEach{ item->

                val id = when (item.modeName) {
                    "Cash" -> {
                        R.drawable.ic_cash
                    }

                    "Bank" -> {
                        R.drawable.ic_bank
                    }

                    "Card" -> {
                        R.drawable.ic_card
                    }

                    "Cheque" -> {
                        R.drawable.ic_cheque
                    }

                    "Net-banking" -> {
                        R.drawable.ic_net_banking
                    }

                    "Upi" -> {
                        R.drawable.ic_upi
                    }

                    else -> {
                        R.drawable.ic_payment
                    }
                }

                AccountItem(
                    isSelected = item.id == currentId,
                    isEditMode = true,
                    name = item.name,
                    symbolId = id,
                    isEditable = false,
                    onSelect = { onSelectPayment(item.id)},
                    onDeleteClick = {},
                    onEditClick = {},
                )
            }

        }
    }
}