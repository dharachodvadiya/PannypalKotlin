package com.indie.apps.pennypal.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.screen.NewEntryFieldItemSection
import com.indie.apps.pennypal.presentation.ui.component.screen.NewEntryTopSelectionButton
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.presentation.viewmodel.NewItemViewModel
import com.indie.apps.pennypal.util.Resource

@Composable
fun NewItemScreen(
    newItemViewModel: NewItemViewModel = hiltViewModel(),
    onMerchantSelect: () -> Unit,
    onPaymentAdd: () -> Unit,
    onNavigationUp: () -> Unit,
    onSaveSuccess: (Boolean, Long, Long) -> Unit,
    merchantData: MerchantNameAndDetails? = null,
    paymentData: Payment? = null,

    modifier: Modifier = Modifier
) {
    if (merchantData != null) {
        newItemViewModel.setMerchantData(merchantData)
    }
    if (paymentData != null) {
        newItemViewModel.setPaymentData(paymentData)
    }

    val paymentList by newItemViewModel.paymentList.collectAsStateWithLifecycle()

    val uiState by newItemViewModel.uiState.collectAsStateWithLifecycle()
    val enableButton by newItemViewModel.enableButton.collectAsStateWithLifecycle()
    val received by newItemViewModel.received.collectAsStateWithLifecycle()
    val merchant by newItemViewModel.merchant.collectAsStateWithLifecycle()
    val payment by newItemViewModel.payment.collectAsStateWithLifecycle()
    val amount by newItemViewModel.amount.collectAsStateWithLifecycle()
    val description by newItemViewModel.description.collectAsStateWithLifecycle()
    val merchantError by newItemViewModel.merchantError.collectAsStateWithLifecycle()
    val paymentError by newItemViewModel.paymentError.collectAsStateWithLifecycle()

    when (uiState) {
        is Resource.Loading -> {
            LoadingScreen()
        }

        is Resource.Success -> {

            Scaffold(topBar = {
                TopBarWithTitle(
                    title = stringResource(id = R.string.new_item), onNavigationUp = {
                        if (enableButton) onNavigationUp()
                    }, contentAlignment = Alignment.Center
                )
            }) { padding ->

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                        .padding(padding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding))
                ) {
                    NewEntryTopSelectionButton(
                        received = received,
                        onReceivedChange = newItemViewModel::onReceivedChange
                    )
                    NewEntryFieldItemSection(
                        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding)),
                        onPaymentAdd = {
                            if (enableButton) onPaymentAdd()
                        },
                        onMerchantSelect = {
                            if (enableButton) onMerchantSelect()
                        },
                        merchantName = merchant?.name,
                        onPaymentSelect = newItemViewModel::onPaymentSelect,
                        paymentList = paymentList,
                        paymentName = payment?.name,
                        amount = amount,
                        description = description,
                        merchantError = merchantError,
                        paymentError = paymentError
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
            LoadingScreen()
        }
    }


}

@Preview
@Composable
private fun NewItemScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        NewItemScreen(onPaymentAdd = {},
            onNavigationUp = {},
            onMerchantSelect = {},
            onSaveSuccess = {a,b,c -> })
    }
}