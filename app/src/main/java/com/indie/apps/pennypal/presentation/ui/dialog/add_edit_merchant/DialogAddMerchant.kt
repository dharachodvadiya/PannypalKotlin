package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_merchant

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.db_entity.Merchant
import com.indie.apps.pennypal.data.module.ContactNumberAndCode
import com.indie.apps.pennypal.presentation.ui.component.composable.common.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DialogAddMerchant(
    addMerchantViewModel: AddEditMerchantViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSaveSuccess: (Merchant?, Boolean) -> Unit,
    onCpp: () -> Unit,
    onContactBook: () -> Unit,
    contactNumberAndCode: ContactNumberAndCode?,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    code: String?
) {

    val context = LocalContext.current
    LaunchedEffect(code) {
        if (code != null) {
            addMerchantViewModel.setCountryCode(code)
        }
    }

    if (contactNumberAndCode != null) {
        LaunchedEffect(contactNumberAndCode) {
            addMerchantViewModel.setContactData(contactNumberAndCode)
        }
    }
    val countryCode by addMerchantViewModel.countryDialCode.collectAsStateWithLifecycle()

    val enableButton by addMerchantViewModel.enableButton.collectAsStateWithLifecycle()
    val merchantName by addMerchantViewModel.merchantName.collectAsStateWithLifecycle()
    val phoneNumber by addMerchantViewModel.phoneNumber.collectAsStateWithLifecycle()
    val description by addMerchantViewModel.description.collectAsStateWithLifecycle()

    val permissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)

    val merchantSaveToast = stringResource(id = R.string.merchant_save_success_toast)
    val merchantEditToast = stringResource(id = R.string.merchant_edit_success_message)

    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterPhoneNumber = remember { FocusRequester() }
    val focusRequesterDescription = remember { FocusRequester() }

    /*  LaunchedEffect(Unit) {
          focusRequesterName.requestFocus()
      }*/

    MyAppDialog(
        title = if (!addMerchantViewModel.getIsEditable()) R.string.add_merchant else R.string.edit_merchant,
        onNavigationUp = {
            if (enableButton) onNavigationUp()
        },
        content = {
            AddMerchantDialogField(
                nameState = merchantName,
                phoneNoState = phoneNumber,
                descState = description,
                onCpp = {
                    addMerchantViewModel.logEvent("add_merchant_cpp")
                    if (enableButton)
                        onCpp()
                },
                onContactBook = {
                    addMerchantViewModel.logEvent("add_merchant_contact_click")
                    if (!permissionState.status.isGranted) {
                        if (!permissionState.status.shouldShowRationale) {
                            val i = Intent()
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            i.addCategory(Intent.CATEGORY_DEFAULT)
                            i.setData(("package:" + context.packageName).toUri())
                            context.startActivity(i)
                        } else {
                            permissionState.launchPermissionRequest()
                        }

                    } else {
                        addMerchantViewModel.logEvent("add_merchant_contact_open")
                        onContactBook()
                    }

                },
                countryCode = countryCode ?: "",
                onDescTextChange = addMerchantViewModel::updateDescText,
                onNameTextChange = addMerchantViewModel::updateNameText,
                onPhoneNoTextChange = addMerchantViewModel::updatePhoneNoText,
                focusRequesterName = focusRequesterName,
                focusRequesterPhoneNumber = focusRequesterPhoneNumber,
                focusRequesterDescription = focusRequesterDescription
            )
        }, bottomContent = {
            BottomSaveButton(
                onClick = {
                    addMerchantViewModel.addOrEditMerchant { merchant, isEdit ->
                        onSaveSuccess(merchant, isEdit)
                        context.showToast(if (isEdit) merchantEditToast else merchantSaveToast)
                    }
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
        DialogAddMerchant(
            onNavigationUp = {},
            onSaveSuccess = { _, _ -> },
            onCpp = {},
            code = null,
            onContactBook = {},
            contactNumberAndCode = null

        )
    }
}