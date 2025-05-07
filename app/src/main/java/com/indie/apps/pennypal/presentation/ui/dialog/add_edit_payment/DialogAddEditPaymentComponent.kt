package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.dimensionResource
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.db_entity.PaymentMode
import com.indie.apps.pennypal.presentation.ui.component.composable.common.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.FlowRowItem
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddPaymentDialogField(
    onModeChange: (Long) -> Unit,
    currentModId: Long,
    paymentModeList: List<PaymentMode>,
    textPaymentState: TextFieldState,
    onPaymentTypeTextChange: (String) -> Unit,
    focusRequesterName: FocusRequester,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.padding)
        )
    ) {
        DialogTextFieldItem(
            textState = textPaymentState,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Payment,
                    contentDescription = "",
                    tint = MyAppTheme.colors.gray1
                )
            },
            placeholder = R.string.add_payment_type_placeholder,
            onTextChange = onPaymentTypeTextChange,
            focusRequester = focusRequesterName
        )

        FlowRow {

            paymentModeList.forEach { item ->
                FlowRowItem(
                    isSelected = currentModId == item.id,
                    text = item.name,
                    onClick = { onModeChange(item.id) })
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
    }
}


