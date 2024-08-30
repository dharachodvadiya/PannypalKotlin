package com.indie.apps.pennypal.presentation.ui.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.component.dialog.AddMerchantDialogField
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.presentation.viewmodel.AddEditMerchantViewModel

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
    val countryCode by addMerchantViewModel.countryDialCode.collectAsStateWithLifecycle()

    LaunchedEffect(code) {
        addMerchantViewModel.setCountryCode(code ?: addMerchantViewModel.getDefaultCurrencyCode())
    }

    LaunchedEffect(editId) {
        addMerchantViewModel.setEditId(editId) // always call after set country code
    }

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


@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogAddMerchant(onNavigationUp = {}, onSaveSuccess = { _, _ -> }, onCpp = {}, code = null
        )
    }
}