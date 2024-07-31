package com.indie.apps.pannypal.presentation.ui.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.component.dialog.AddMerchantDialogField
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.AddMerchantViewModel
import com.indie.apps.pannypal.util.ErrorMessage

@Composable
fun DialogAddMerchant(
    addMerchantViewModel: AddMerchantViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSaveSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val merchantName by remember { mutableStateOf(TextFieldState()) }
    val phoneNumber by remember { mutableStateOf(TextFieldState()) }
    val description by remember { mutableStateOf(TextFieldState()) }

    MyAppDialog(
        title = R.string.add_merchant,
        onNavigationUp = onNavigationUp,
        content = {
            AddMerchantDialogField(
                nameState = merchantName,
                phoneNoState = phoneNumber,
                descState = description
            )
        },
        bottomContent = {
            BottomSaveButton(
                onClick = {
                    addMerchantViewModel.addMerchant(
                        merchant = Merchant(
                            name = merchantName.text,
                            phoneNumber = phoneNumber.text,
                            details = description.text,
                            dateInMilli = System.currentTimeMillis()
                        ),
                        onSuccess = onSaveSuccess,
                        onFail = {
                            when (it) {
                                ErrorMessage.MERCHANT_NAME_EMPTY, ErrorMessage.MERCHANT_EXIST -> merchantName.setError(
                                    it
                                )

                                ErrorMessage.PHONE_NO_INVALID -> phoneNumber.setError(it)
                            }
                        }
                    )
                },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
            )
        },
        modifier = modifier
    )
}


@Preview()
@Composable
private fun MyAppDialogPreview() {
    PannyPalTheme {
        DialogAddMerchant(
            onNavigationUp = {},
            onSaveSuccess = {}
        )
    }
}