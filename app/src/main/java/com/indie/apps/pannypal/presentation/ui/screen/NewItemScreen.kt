package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pannypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pannypal.presentation.ui.component.screen.NewEntryFieldItemSection
import com.indie.apps.pannypal.presentation.ui.component.screen.NewEntryTopSelectionButton
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.NewItemViewModel
import com.indie.apps.pannypal.util.Resource

@Composable
fun NewItemScreen(
    newItemViewModel: NewItemViewModel = hiltViewModel(),
    onMerchantSelect: () -> Unit,
    onPaymentAdd: () -> Unit,
    onNavigationUp: () -> Unit,
    onSaveSuccess: (Boolean) -> Unit,
    merchant: MerchantNameAndDetails? = null,
    payment: Payment? = null,

    modifier: Modifier = Modifier
) {
    if (merchant != null) {
        newItemViewModel.setMerchantData(merchant)
    }
    if (payment != null) {
        newItemViewModel.setPaymentData(payment)
    }

    val paymentList by newItemViewModel.paymentList.collectAsStateWithLifecycle()

    val uiState by newItemViewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is Resource.Loading -> {
            LoadingScreen()
        }

        is Resource.Success -> {

            Scaffold(topBar = {
                TopBarWithTitle(
                    title = stringResource(id = R.string.new_item), onNavigationUp = {
                        if (newItemViewModel.enableButton) onNavigationUp()
                    }, contentAlignment = Alignment.Center
                )
            }) { padding ->

                Column(
                    modifier = modifier
                        .padding(padding)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding))
                ) {
                    NewEntryTopSelectionButton(
                        received = newItemViewModel.received,
                        onReceivedChange = newItemViewModel::onReceivedChange
                    )
                    NewEntryFieldItemSection(
                        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding)),
                        onPaymentAdd = {
                            if (newItemViewModel.enableButton) onPaymentAdd()
                        },
                        onMerchantSelect = {
                            if (newItemViewModel.enableButton) onMerchantSelect()
                        },
                        merchantName = newItemViewModel.merchant?.name,
                        onPaymentSelect = newItemViewModel::onPaymentSelect,
                        paymentList = paymentList,
                        paymentName = newItemViewModel.payment?.name,
                        amount = newItemViewModel.amount,
                        description = newItemViewModel.description,
                        merchantError = newItemViewModel.merchantError,
                        paymentError = newItemViewModel.paymentError
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BottomSaveButton(
                        onClick = {
                            newItemViewModel.addOrEditMerchantData(onSuccess = {
                                onSaveSuccess(it)
                            })
                        },
                        enabled = newItemViewModel.enableButton,
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
    PannyPalTheme {
        NewItemScreen(onPaymentAdd = {},
            onNavigationUp = {},
            onMerchantSelect = {},
            onSaveSuccess = {})
    }
}