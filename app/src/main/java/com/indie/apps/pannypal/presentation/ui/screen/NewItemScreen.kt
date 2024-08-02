package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pannypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pannypal.presentation.ui.component.screen.NewEntryFieldItemSection
import com.indie.apps.pannypal.presentation.ui.component.screen.NewEntryTopSelectionButton
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun NewItemScreen(
    onMerchantSelect:()-> Unit,
    onPaymentAdd: ()-> Unit,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier,
    merchant: MerchantNameAndDetails? = null,
    payment: Payment? = null,
){
// TODO setProfile data
    Scaffold(
        topBar = {
            TopBarWithTitle(
                title = stringResource(id = R.string.new_item),
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center
            )
        }
    ) { padding ->

        if (merchant != null) {
            println("aaaaaa ${merchant.toString()}")
        }

        if (payment != null) {
            println("aaaaaa ${payment.toString()}")
        }

        var received = remember { (mutableStateOf(false)) }
        Column(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        ) {
            NewEntryTopSelectionButton(received)
            NewEntryFieldItemSection(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.padding)),
                onPaymentAdd = onPaymentAdd,
                onMerchantSelect = onMerchantSelect
            )
            Spacer(modifier = Modifier.weight(1f))
            BottomSaveButton(
                onClick = {},
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding)))
        }
    }
}

@Preview
@Composable
private fun NewItemScreenPreview() {
    PannyPalTheme {
        NewItemScreen({},{},{})
    }
}