package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.entity.PaymentMode
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddPaymentDialogField(
    onModeChange: (Long) -> Unit,
    currentModId: Long,
    paymentModeList: List<PaymentMode>,
    textPaymentState: TextFieldState
) {
    DialogTextFieldItem(
        textState = textPaymentState,
        imageVector = Icons.Default.Payment,
        placeholder = R.string.add_payment_type_placeholder
    )

    FlowRow {

        paymentModeList.forEach { item ->
            PaymentModeItem(
                isSelected = currentModId == item.id,
                text = item.name,
                onClick = { onModeChange(item.id) })
        }
    }
    Spacer(modifier = Modifier.fillMaxHeight(0.1f))
}

@Composable
fun PaymentModeItem(
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) MyAppTheme.colors.lightBlue1 else MyAppTheme.colors.gray2

    val shape = RoundedCornerShape(100.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.item_padding))
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = color
                ),
                shape = shape
            )
            .background(color = MyAppTheme.colors.transparent, shape = shape)
            .clip(shape = shape)
            .clickable { onClick() }
            .padding(
                horizontal = dimensionResource(R.dimen.bottom_bar_item_horizontal_padding),
                vertical = dimensionResource(R.dimen.bottom_bar_item_vertical_padding)
            )

    ) {
        CustomText(
            text = text,
            color = color,
            style = MyAppTheme.typography.Medium40
        )
    }

}

