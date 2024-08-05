package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataBottomBar
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataDateItem
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataExpenseAmount
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataIncomeAmount
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataTopBar
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.MerchantDataViewModel

@Composable
fun MerchantDataScreen(
    merchantDataViewModel: MerchantDataViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    val merchantState by merchantDataViewModel.merchantState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            MerchantDataTopBar(
                onClick =onProfileClick,
                onNavigationUp = onNavigationUp
            )
        }
    ){padding->
        Column(
            modifier = modifier
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding))
            ) {
                items(15) { index ->

                    if(index %3 == 0)
                    {
                        MerchantDataDateItem()

                        MerchantDataIncomeAmount(
                            onClick = {},
                            onLongClick = {}
                        )
                    }else{
                        MerchantDataExpenseAmount(
                            onClick = {},
                            onLongClick = {}
                        )
                    }
                }
            }
            MerchantDataBottomBar(
                isEditable = false,
                isDeletable = false,
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun MerchantDataScreenPreview() {
    PannyPalTheme {
        MerchantDataScreen(
            onProfileClick = {},
            onNavigationUp = {}
        )
    }
}