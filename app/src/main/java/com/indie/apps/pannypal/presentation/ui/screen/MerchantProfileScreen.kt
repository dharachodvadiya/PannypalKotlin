package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantProfileBottomSection
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantProfileTopSection
import com.indie.apps.pannypal.presentation.ui.component.screen.ProfileSection2
import com.indie.apps.pannypal.presentation.ui.component.screen.ProfileTopSection
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.MerchantProfileViewModel
import com.indie.apps.pannypal.util.Resource

@Composable
fun  MerchantProfileScreen(
    merchantProfileViewModel: MerchantProfileViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier) {

    val uiState by merchantProfileViewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is Resource.Loading -> {
            LoadingScreen()
        }

        is Resource.Success -> {
            uiState.data?.let {
                MerchantProfileData(
                    onNavigationUp = onNavigationUp,
                    merchant = uiState.data!!,
                    modifier = modifier
                )
            }
        }

        is Resource.Error -> {
            LoadingScreen()
        }
    }


}

@Composable
fun MerchantProfileData(
    onNavigationUp: () -> Unit,
    merchant: Merchant,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            TopBarWithTitle(
                title = "",
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.white
            )
        }
    ) { padding ->

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(padding)
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        ) {
            MerchantProfileTopSection(
                name = merchant.name,
                description = merchant.details ?: "",
                Modifier.height(screenHeight * 0.35f)
            )
            MerchantProfileBottomSection(
                phoneNo = if(merchant.phoneNumber.isNullOrEmpty()) "" else "${merchant.countryCode} ${merchant.phoneNumber}"
            )
        }

    }
}

@Preview
@Composable
private fun MerchantProfileScreenPreview() {
    PannyPalTheme {
        MerchantProfileScreen(
            onNavigationUp = {}
        )
    }
}