package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.PaymentWithMode
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun PaymentModeDefaultItem(
    paymentModeName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
            .background(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                color = MyAppTheme.colors.itemBg
            )
            .padding(dimensionResource(id = R.dimen.padding))
    ) {
        Text(
            text = stringResource(id = R.string.default_payment_mode),
            style = MyAppTheme.typography.Medium40,
            color = MyAppTheme.colors.gray2
        )
        Text(
            text = paymentModeName,
            style = MyAppTheme.typography.Semibold50,
            color = MyAppTheme.colors.black
        )
    }
}

@Composable
fun AccountBankItem(
    defaultPaymentId: Long,
    isEditMode: Boolean,
    dataList: List<PaymentWithMode>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
    ) {
        AccountHeadingItem()
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )
                .padding(dimensionResource(id = R.dimen.padding))
        ) {

            dataList.forEach(){item->
                val id = when(item.modeName)
                {
                    "Bank" -> { R.drawable.ic_bank}
                    "Card" -> { R.drawable.ic_card}
                    "Cheque" -> {R.drawable.ic_cheque}
                    "Net-banking" -> { R.drawable.ic_net_banking}
                    "Upi" -> {R.drawable.ic_upi}
                    else -> {R.drawable.ic_payment}
                }
                AccountItem(
                    isSelected = item.id == defaultPaymentId,
                    isEditMode = isEditMode,
                    name = item.name,
                    symbolId = id,
                    isEditable = item.preAdded == 0)
            }
        }
    }
}

@Composable
fun AccountHeadingItem(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.bank),
        style = MyAppTheme.typography.Regular51,
        color = MyAppTheme.colors.gray1,
        modifier = modifier
    )
}

@Composable
fun AccountItem(
    isSelected: Boolean,
    isEditMode: Boolean,
    isEditable: Boolean,
    name: String,
    @DrawableRes symbolId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_padding))
    ) {
        if (isEditMode)
            RadioButton(selected = isSelected, onClick = { /*TODO*/ })
        Icon(
            painter = painterResource(symbolId),
            contentDescription = "bank",
            tint = MyAppTheme.colors.lightBlue1,
            modifier = Modifier.size(dimensionResource(id = R.dimen.small_icon_size))
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_padding)))
        Text(
            text = name,
            style = MyAppTheme.typography.Semibold52_5,
            color = MyAppTheme.colors.black
        )

        if (isEditMode && isEditable) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "edit"
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.item_padding)))

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete"
            )
        }
    }
}

@Composable
fun AccountCashItem(
    defaultPaymentId: Long,
    isEditMode: Boolean,
    dataList: List<PaymentWithMode>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
    ) {
        AccountHeadingItem()
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )
                .padding(dimensionResource(id = R.dimen.padding))
        ) {
            dataList.forEach(){item->

                AccountItem(
                    isSelected = item.id == defaultPaymentId,
                    isEditMode = isEditMode,
                    name = item.name,
                    symbolId = R.drawable.ic_cash,
                    isEditable = item.preAdded == 0 )
            }
        }
    }
}