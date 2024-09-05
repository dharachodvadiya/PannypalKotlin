package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun PaymentScreen(
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    bottomPadding: PaddingValues,
    onModeChange: (Boolean) -> Unit
) {
    val isEditMode by paymentViewModel.isInEditMode.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onModeChange(false)
    }

    Scaffold(
        topBar = {
            TopBarWithTitle(
                isBackEnable = isEditMode,
                onNavigationUp = {
                    paymentViewModel.setEditMode(false)
                    onModeChange(false)
                },
                title = if(isEditMode) stringResource(id = R.string.edit_account_details) else stringResource(id = R.string.accounts),
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.transparent,
                trailingContent = {
                    if(isEditMode){

                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "done",
                            tint = MyAppTheme.colors.black,
                            modifier = Modifier
                                .size(25.dp)
                                .clickable {
                                    paymentViewModel.saveEditedData()
                                    onModeChange(false)
                                }
                        )

                    }else{
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "edit",
                            tint = MyAppTheme.colors.black,
                            modifier = Modifier
                                .size(25.dp)
                                .clickable {
                                    paymentViewModel.setEditMode(true)
                                    onModeChange(true)
                                }
                        )
                    }

                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
        ) {
            if(!isEditMode){
                PaymentModeDefaultItem()
            }

            AccountBankItem(isEditMode = isEditMode)
            AccountCashItem(isEditMode = isEditMode)
        }

    }
}

@Preview
@Composable
private fun PaymentScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        PaymentScreen(
            bottomPadding = PaddingValues(0.dp),
            onModeChange = {}
        )
    }
}
