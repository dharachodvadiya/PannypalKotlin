package com.indie.apps.pennypal.presentation.ui.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Resource

@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    onCurrencyChange: (String) -> Unit,
    onDefaultPaymentChange: (Long) -> Unit,
){

    val uiState by settingViewModel.uiState.collectAsStateWithLifecycle()
    val generalList by settingViewModel.generalList.collectAsStateWithLifecycle()
    val moreList by settingViewModel.moreList.collectAsStateWithLifecycle()


    when(uiState){
        is Resource.Error ->{
            LoadingWithProgress()
        }
        is Resource.Loading -> {
            LoadingWithProgress()
        }
        is Resource.Success -> {

            SettingScreenData(
                generalList = generalList,
                moreList = moreList,
                onSelect  = {
                    settingViewModel.onSelectOption(
                        item = it,
                        onCurrencyChange = onCurrencyChange,
                        onDefaultPaymentChange = onDefaultPaymentChange)
                }
            )
        }
    }
}

@Composable
fun SettingScreenData(
    generalList : List<MoreItem>,
    moreList : List<MoreItem>,
    onSelect: (MoreItem) -> Unit
){
    Scaffold(
        topBar = {
            TopBarWithTitle(
                isBackEnable = false,
                onNavigationUp = {},
                title =stringResource(id = R.string.setting),
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.transparent,
            )
        }
    ) { innerPadding ->

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
        ) {
            /*val generalList = listOf(
                MoreItem(title = R.string.currency_and_format, subTitle = "USD($)"),
                MoreItem(title = R.string.default_payment_mode, subTitle =  "Cash"),
            )*/
            SettingTypeItem(
                titleId = R.string.general,
                dataList = generalList,
                onSelect = onSelect,
                arrowIconEnable = true
            )

            /*val moreList = listOf(
                MoreItem(title = R.string.share, icon = R.drawable.ic_share),
                MoreItem(title = R.string.rate, icon = R.drawable.ic_rate),
                MoreItem(title = R.string.privacy_policy, icon = R.drawable.ic_privacy),
                MoreItem(title = R.string.contact_us, icon = R.drawable.ic_contact_us),
            )*/
            SettingTypeItem(
                titleId = R.string.more,
                dataList = moreList,
                onSelect = onSelect,
                arrowIconEnable = false
            )
        }

    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        SettingScreen(
            onDefaultPaymentChange = {},
            onCurrencyChange = {}
        )
    }
}
