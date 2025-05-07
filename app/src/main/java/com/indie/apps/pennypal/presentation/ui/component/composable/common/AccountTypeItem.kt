package com.indie.apps.pennypal.presentation.ui.component.composable.common

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.payment.PaymentWithMode
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.screen.payment.AccountHeadingItem
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.internanal.method.getPaymentModeIcon
import kotlinx.coroutines.launch


@Composable
fun AccountTypeItem(
    @StringRes titleId: Int,
    selectPaymentId: Long,
    editAnimPaymentId: Long = 0L,
    editAnimRun: Boolean = false,
    isSelectable: Boolean,
    isEditable: Boolean,
    dataList: List<PaymentWithMode>,
    onSelect: (PaymentWithMode) -> Unit = {},
    onEditClick: (Long) -> Unit = {},
    onDeleteClick: (PaymentWithMode) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        val scope = rememberCoroutineScope()
        val baseColor = MyAppTheme.colors.itemBg
        val targetAnimColor = MyAppTheme.colors.lightBlue1

        AccountHeadingItem(titleId)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .roundedCornerBackground(MyAppTheme.colors.itemBg)/*.background(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.round_corner)),
                    color = MyAppTheme.colors.itemBg
                )*/
                .padding(dimensionResource(id = R.dimen.padding))
        ) {

            dataList.forEach { item ->

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
                            targetValue = baseColor, animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                        )
                    }
                    modifier.background(itemAnimateColor.value)
                } else {
                    Modifier
                }

                AccountItem(
                    isSelected = item.id == selectPaymentId,
                    isSelectable = isSelectable,
                    isEditable = isEditable,
                    name = item.name,
                    symbolId = getPaymentModeIcon(item.name),
                    onSelect = { onSelect(item) },
                    modifier = modifierColor,
                    onDeleteClick = { onDeleteClick(item) },
                    onEditClick = { onEditClick(item.id) },
                    isPreAdded = item.preAdded == 1
                )
            }
        }
    }
}