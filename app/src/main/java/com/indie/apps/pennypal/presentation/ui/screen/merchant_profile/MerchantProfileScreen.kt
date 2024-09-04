package com.indie.apps.pennypal.presentation.ui.screen.merchant_profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.data.entity.Merchant
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Resource

@Composable
fun MerchantProfileScreen(
    merchantProfileViewModel: MerchantProfileViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val uiState by merchantProfileViewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is Resource.Loading -> {
            LoadingWithProgress()
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
            LoadingWithProgress()
        }
    }


}

@Composable
fun MerchantProfileData(
    onNavigationUp: () -> Unit,
    merchant: Merchant,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopBarWithTitle(
                title = "",
                onNavigationUp = onNavigationUp,
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.itemBg
            )
        }
    ) { padding ->

        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Column(
            modifier = modifier
                .fillMaxHeight()
                .background(MyAppTheme.colors.brandBg)
                .padding(padding)
        ) {
            MerchantProfileTopSection(
                name = merchant.name,
                description = merchant.details ?: "",
                Modifier.height(screenHeight * 0.3f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            MerchantProfileBottomSection(
                phoneNo = if (merchant.phoneNumber.isNullOrEmpty()) "" else "${merchant.countryCode} ${merchant.phoneNumber}"
            )
        }

    }
}

@Preview
@Composable
private fun MerchantProfileScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        MerchantProfileScreen(
            onNavigationUp = {}
        )
    }
}