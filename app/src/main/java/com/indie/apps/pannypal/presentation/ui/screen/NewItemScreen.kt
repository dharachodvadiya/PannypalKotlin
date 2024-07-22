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
import com.indie.apps.pannypal.presentation.ui.component.MyAppTopBar
import com.indie.apps.pannypal.presentation.ui.component.screen.NewEntryFieldItemSection
import com.indie.apps.pannypal.presentation.ui.component.screen.NewEntrySaveButton
import com.indie.apps.pannypal.presentation.ui.component.screen.NewEntryTopSelectionButton
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun NewItemScreen(
    onMerchantSelect:()-> Unit,
    onPaymentAdd: ()-> Unit,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier
){
// TODO setProfile data
    Scaffold(
        topBar = {
            MyAppTopBar(
                title = stringResource(id = R.string.new_item),
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center
            )
        }
    ) { padding ->

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
            NewEntrySaveButton(
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