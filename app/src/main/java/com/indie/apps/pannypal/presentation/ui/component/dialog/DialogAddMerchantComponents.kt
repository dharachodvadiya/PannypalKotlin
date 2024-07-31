package com.indie.apps.pannypal.presentation.ui.component.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppTextField
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMerchantDialogField(
    nameState: TextFieldState,
    phoneNoState: TextFieldState,
    descState: TextFieldState,
    onCpp: () -> Unit,
    countryCode: String = "+00"
) {
    Column {
        DialogTextFieldItem(
            textState = nameState,
            imageVector = Icons.Default.PersonOutline,
            placeholder = R.string.merchant_name_placeholder
        )
        DialogTextFieldItem(
            textState = phoneNoState,
            imageVector = Icons.Default.Phone,
            placeholder = R.string.phone_number_placeholder,
            textLeadingContent = {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onCpp() }
                ) {
                    Text(text = countryCode)
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }

            },
            keyboardType = KeyboardType.Number
        )
        DialogTextFieldItem(
            textState = descState,
            imageVector = Icons.Default.Details,
            placeholder = R.string.description_placeholder
        )
    }
}