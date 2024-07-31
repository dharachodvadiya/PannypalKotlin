package com.indie.apps.pannypal.presentation.ui.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.component.dialog.AddMerchantDialogField
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.AddMerchantViewModel
import com.mcode.ccp.data.utils.getDefaultPhoneCode

@Composable
fun DialogAddMerchant(
    addMerchantViewModel: AddMerchantViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSaveSuccess: () -> Unit,
    onCpp: () -> Unit,
    modifier: Modifier = Modifier,
    code: String?
) {

    addMerchantViewModel.countryCode = code ?: getDefaultPhoneCode(LocalContext.current)

    MyAppDialog(title = R.string.add_merchant, onNavigationUp = onNavigationUp, content = {
        AddMerchantDialogField(
            nameState = addMerchantViewModel.merchantName,
            phoneNoState = addMerchantViewModel.phoneNumber,
            descState = addMerchantViewModel.description,
            onCpp = {
                onCpp()
            },
            countryCode = addMerchantViewModel.countryCode!!
        )
    }, bottomContent = {
        BottomSaveButton(
            onClick = {
                addMerchantViewModel.addMerchant(
                    onSuccess = onSaveSuccess
                )
            }, modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
        )
    }, modifier = modifier
    )
}


@Preview()
@Composable
private fun MyAppDialogPreview() {
    PannyPalTheme {
        DialogAddMerchant(onNavigationUp = {}, onSaveSuccess = {}, onCpp = {}, code = null
        )
    }
}