package com.indie.apps.pennypal.presentation.ui.screen.new_item

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.entity.Category
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.state.rememberImeState
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
    onSaveSuccess: (Boolean, Long, Long) -> Unit,
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

    var openDiscardDialog by remember { mutableStateOf(false) }

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

                val imeState by rememberImeState {}
                val scrollState = rememberScrollState()

                LaunchedEffect(key1 = imeState) {
                    if (imeState && haveFocus) {
                        scrollState.animateScrollTo(scrollState.maxValue, tween(300))
                    }
                }

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                        .padding(padding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding))
                        .verticalScroll(scrollState)
                        .onFocusEvent {
                            haveFocus = it.isFocused
                        }
                ) {
                    NewEntryTopSelectionButton(
                        received = received,
                        onReceivedChange = newItemViewModel::onReceivedChange
                    )
                    NewEntryFieldItemSection(
                        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding)),
                        onMerchantSelect = {
                            if (enableButton) {
                                focusManager.clearFocus()
                                onMerchantSelect()
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
                        paymentName = payment?.name,
                        categoryName = category?.name,
                        amount = amount,
                        description = description,
                        merchantError = merchantError,
                        paymentError = paymentError,
                        categoryError = categoryError,
                        isMerchantLock = isMerchantLock
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BottomSaveButton(
                        onClick = {
                            newItemViewModel.addOrEditMerchantData(onSuccess = onSaveSuccess)
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