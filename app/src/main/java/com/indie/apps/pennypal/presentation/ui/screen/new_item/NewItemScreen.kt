package com.indie.apps.pennypal.presentation.ui.screen.new_item

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomDatePickerDialog
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomTimePickerDialog
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Resource

@Composable
fun NewItemScreen(
    newItemViewModel: NewItemViewModel = hiltViewModel(),
    onMerchantSelect: () -> Unit,
    onPaymentSelect: (Long?) -> Unit,
    onCategorySelect: (Long?, Int) -> Unit,
    onNavigationUp: () -> Unit,
    isMerchantLock: Boolean,
    onSaveSuccess: (Boolean, Long, Long?) -> Unit,
    merchantData: MerchantNameAndDetails? = null,
    paymentData: Payment? = null,
    categoryData: Category? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    LaunchedEffect(merchantData) {
        if (merchantData != null) {
            newItemViewModel.setMerchantData(merchantData)
        }
    }

    LaunchedEffect(paymentData) {
        if (paymentData != null) {
            newItemViewModel.setPaymentData(paymentData)
        }
    }

    LaunchedEffect(categoryData) {
        if (categoryData != null) {
            newItemViewModel.setCategory(categoryData)
        }
    }

    val currentTimeInMilli by newItemViewModel.currentTimeInMilli.collectAsStateWithLifecycle()
    val uiState by newItemViewModel.uiState.collectAsStateWithLifecycle()
    val enableButton by newItemViewModel.enableButton.collectAsStateWithLifecycle()
    val received by newItemViewModel.received.collectAsStateWithLifecycle()
    val merchant by newItemViewModel.merchant.collectAsStateWithLifecycle()
    val payment by newItemViewModel.payment.collectAsStateWithLifecycle()
    val category by newItemViewModel.category.collectAsStateWithLifecycle()
    val amount by newItemViewModel.amount.collectAsStateWithLifecycle()
    val description by newItemViewModel.description.collectAsStateWithLifecycle()
    val merchantError by newItemViewModel.merchantError.collectAsStateWithLifecycle()
    val paymentError by newItemViewModel.paymentError.collectAsStateWithLifecycle()
    val categoryError by newItemViewModel.categoryError.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    var haveFocus by remember { mutableStateOf(false) }

    var openDateDialog by remember { mutableStateOf(false) }
    var openTimeDialog by remember { mutableStateOf(false) }

    var openDiscardDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val merchantChangeToastMessage = stringResource(R.string.can_not_change_merchant)
    val merchantDataEditToast = stringResource(id = R.string.merchant_data_edit_success_message)
    val merchantDataSaveToast = stringResource(id = R.string.merchant_data_save_success_message)

    BackHandler {
        if (newItemViewModel.isEditData()) {
            openDiscardDialog = true
        } else {
            onNavigationUp()
        }
    }

    when (uiState) {
        is Resource.Loading -> {
            LoadingWithProgress()
        }

        is Resource.Success -> {

            val title = if (newItemViewModel.merchantEditId == 0L)
                stringResource(id = R.string.new_item)
            else
                stringResource(id = R.string.edit_merchant_data)

            Scaffold(topBar = {
                TopBarWithTitle(
                    title = title,
                    onNavigationUp = {
                        if (enableButton) {
                            if (newItemViewModel.isEditData()) {
                                openDiscardDialog = true
                            } else {
                                onNavigationUp()
                            }
                        }
                    }, contentAlignment = Alignment.Center
                )
            }) { padding ->

                //val imeState by rememberImeState {}
                val scrollState = rememberScrollState()

                /* LaunchedEffect(key1 = haveFocus) {
                     if (haveFocus) {
                         scrollState.animateScrollTo(scrollState.maxValue, tween(300))
                     }
                 }*/

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                        .padding(padding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding))
                        .imePadding()
                        .verticalScroll(scrollState)
                    /*.onFocusEvent {
                       // haveFocus = it.isFocused
                    }*/
                ) {
                    NewEntryTopSelectionButton(
                        received = received,
                        onReceivedChange = newItemViewModel::onReceivedChange
                    )
                    NewEntryFieldItemSection(
                        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding)),
                        currentTimeInMilli = currentTimeInMilli,
                        onMerchantSelect = {
                            if (enableButton) {
                                if (isMerchantLock) {
                                    context.showToast(merchantChangeToastMessage)
                                } else {
                                    focusManager.clearFocus()
                                    onMerchantSelect()
                                }
                            }
                        },
                        merchantName = merchant?.name,
                        onPaymentSelect = {
                            if (enableButton) {
                                focusManager.clearFocus()
                                onPaymentSelect(payment?.id)
                            }
                        },
                        onCategorySelect = {
                            if (enableButton) {
                                focusManager.clearFocus()
                                onCategorySelect(category?.id, if (received) 1 else -1)
                            }
                        },
                        onDateSelect = { openDateDialog = true },
                        onTimeSelect = { openTimeDialog = true },
                        paymentName = payment?.name,
                        category = category,
                        amount = amount,
                        description = description,
                        merchantError = merchantError.asString(),
                        paymentError = paymentError.asString(),
                        categoryError = categoryError.asString(),
                        isMerchantLock = isMerchantLock,
                        onAmountTextChange = newItemViewModel::updateAmountText,
                        onDescTextChange = newItemViewModel::updateDescText
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BottomSaveButton(
                        onClick = {
                            newItemViewModel.addOrEditMerchantData() { isEdit, id, merchantId ->
                                onSaveSuccess(isEdit, id, merchantId)
                                if (isEdit)
                                    context.showToast(merchantDataEditToast)
                                else
                                    context.showToast(merchantDataSaveToast)

                            }
                        },
                        enabled = enableButton,
                        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding))
                    )
                }
            }
        }

        is Resource.Error -> {
            LoadingWithProgress()
        }
    }

    if (openDiscardDialog) {
        ConfirmationDialog(
            dialogTitle = R.string.discard_dialog_title,
            dialogText = R.string.discard_dialog_text,
            onConfirmation = {
                openDiscardDialog = false
                onNavigationUp()
            },
            onDismissRequest = {
                openDiscardDialog = false
            },
            positiveText = R.string.discard,
            negativeText = R.string.cancel
        )
    }

    if (openDateDialog) {
        CustomDatePickerDialog(
            currentTimeInMilli = currentTimeInMilli,
            onDateSelected = {
                newItemViewModel.setDateAndTime(it.timeInMillis)
                openDateDialog = false
            },
            onDismiss = {
                openDateDialog = false
            }
        )
    }

    if (openTimeDialog) {
        CustomTimePickerDialog(
            currentTimeInMilli = currentTimeInMilli,
            onTimeSelected = {
                newItemViewModel.setDateAndTime(it.timeInMillis)
                openTimeDialog = false
            },
            onDismiss = {
                openTimeDialog = false
            }
        )
    }


}

@Preview
@Composable
private fun NewItemScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        NewItemScreen(
            onPaymentSelect = {},
            onNavigationUp = {},
            onMerchantSelect = {},
            onCategorySelect = { _, _ -> },
            onSaveSuccess = { _, _, _ -> },
            isMerchantLock = false
        )
    }
}