package com.indie.apps.pannypal.presentation.ui.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.component.dialog.AddMerchantDialogField
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.AddEditMerchantViewModel
import com.mcode.ccp.data.utils.getDefaultPhoneCode

@Composable
fun DialogAddMerchant(
    addMerchantViewModel: AddEditMerchantViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSaveSuccess: (Merchant?, Boolean) -> Unit,
    onCpp: () -> Unit,
    modifier: Modifier = Modifier,
    code: String?,
    editId: Long? = null,
) {

    val countryCode by addMerchantViewModel.countryCode.collectAsStateWithLifecycle()
    addMerchantViewModel.setCountryCode(code ?: getDefaultPhoneCode(LocalContext.current))
    addMerchantViewModel.setEditId(editId) // always call after set country code

    val enableButton by addMerchantViewModel.enableButton.collectAsStateWithLifecycle()
    val merchantName by addMerchantViewModel.merchantName.collectAsStateWithLifecycle()
    val phoneNumber by addMerchantViewModel.phoneNumber.collectAsStateWithLifecycle()
    val description by addMerchantViewModel.description.collectAsStateWithLifecycle()

    MyAppDialog(title = if (editId == null) R.string.add_merchant else R.string.edit_merchant,
        onNavigationUp = {
            if (enableButton) onNavigationUp()
        },
        content = {
            AddMerchantDialogField(
                nameState = merchantName,
                phoneNoState = phoneNumber,
                descState = description,
                onCpp = {
                    if (enableButton)
                        onCpp()
                },
                countryCode = countryCode
            )
        }, bottomContent = {
            BottomSaveButton(
                onClick = {
                    addMerchantViewModel.addOrEditMerchant(
                        onSuccess = onSaveSuccess
                    )
                },
                enabled = enableButton,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
            )
        }, modifier = modifier
    )
}


@Preview()
@Composable
private fun MyAppDialogPreview() {
    PannyPalTheme {
        DialogAddMerchant(onNavigationUp = {}, onSaveSuccess = { a, b -> }, onCpp = {}, code = null
        )
    }
}