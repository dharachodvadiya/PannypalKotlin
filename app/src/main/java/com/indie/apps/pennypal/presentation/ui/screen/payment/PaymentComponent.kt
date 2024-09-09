package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.PaymentWithMode
import com.indie.apps.pennypal.presentation.ui.component.AccountItem
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.launch

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
    editAnimPaymentId: Long,
    editAnimRun: Boolean,
    isEditMode: Boolean,
    dataList: List<PaymentWithMode>,
    onSelect: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (PaymentWithMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
    ) {
        val scope = rememberCoroutineScope()
        val baseColor = MyAppTheme.colors.itemBg
        val targetAnimColor = MyAppTheme.colors.lightBlue1

        AccountHeadingItem(R.string.bank)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )
                .padding(dimensionResource(id = R.dimen.padding))
        ) {

            dataList.forEach() { item ->

                val itemAnimateColor = remember {
                    androidx.compose.animation.Animatable(baseColor)
                }
                val modifierColor = if (editAnimPaymentId == item.id && editAnimRun) {

                    scope.launch {
                        itemAnimateColor.animateTo(
                            targetValue = targetAnimColor,
                            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                        )
                        itemAnimateColor.animateTo(
                            targetValue = baseColor,
                            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                        )
                    }
                    modifier.background(itemAnimateColor.value)
                } else {
                    Modifier
                }

                val id = when (item.modeName) {
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
                    isSelected = item.id == defaultPaymentId,
                    isEditMode = isEditMode,
                    name = item.name,
                    symbolId = id,
                    isEditable = item.preAdded == 0,
                    onSelect = { onSelect(item.id) },
                    onDeleteClick = { onDeleteClick(item) },
                    onEditClick = { onEditClick(item.id) },
                    modifier = modifierColor
                )
            }
        }
    }
}

@Composable
fun AccountHeadingItem(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = title),
        style = MyAppTheme.typography.Regular51,
        color = MyAppTheme.colors.gray1,
        modifier = modifier
    )
}


@Composable
fun AccountCashItem(
    defaultPaymentId: Long,
    editAnimPaymentId: Long,
    editAnimRun: Boolean,
    isEditMode: Boolean,
    dataList: List<PaymentWithMode>,
    onSelect: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (PaymentWithMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding))
    ) {
        val scope = rememberCoroutineScope()
        val baseColor = MyAppTheme.colors.itemBg
        val targetAnimColor = MyAppTheme.colors.lightBlue1
        AccountHeadingItem(R.string.cash)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )
                .padding(dimensionResource(id = R.dimen.padding))
        ) {
            dataList.forEach() { item ->
                val itemAnimateColor = remember {
                    androidx.compose.animation.Animatable(baseColor)
                }
                val modifierColor = if (editAnimPaymentId == item.id && editAnimRun) {
                    scope.launch {
                        itemAnimateColor.animateTo(
                            targetValue = targetAnimColor,
                            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                        )
                        itemAnimateColor.animateTo(
                            targetValue = baseColor,
                            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                        )
                    }
                    Modifier
                } else {
                    Modifier
                }

                AccountItem(
                    isSelected = item.id == defaultPaymentId,
                    isEditMode = isEditMode,
                    name = item.name,
                    symbolId = R.drawable.ic_cash,
                    isEditable = item.preAdded == 0,
                    onSelect = { onSelect(item.id) },
                    onDeleteClick = { onDeleteClick(item) },
                    onEditClick = { onEditClick(item.id) },
                    modifier = modifierColor
                )
            }
        }
    }
}