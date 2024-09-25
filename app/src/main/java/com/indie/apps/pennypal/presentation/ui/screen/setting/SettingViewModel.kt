package com.indie.apps.pennypal.presentation.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.domain.usecase.GetGeneralSettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    getGeneralSettingUseCase: GetGeneralSettingUseCase,
) : ViewModel() {

    private var currPaymentId = 0L
    private var currCountryCode = "US"

    val generalList = getGeneralSettingUseCase
        .loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
    val moreList = MutableStateFlow(
        listOf(
            MoreItem(title = R.string.share, icon = R.drawable.ic_share),
            MoreItem(title = R.string.rate, icon = R.drawable.ic_rate),
            MoreItem(title = R.string.privacy_policy, icon = R.drawable.ic_privacy),
            MoreItem(title = R.string.contact_us, icon = R.drawable.ic_contact_us),
        )
    )

    fun onSelectOption(
        item: MoreItem,
        onDefaultPaymentChange: (Long) -> Unit,
        onCurrencyChange: (String) -> Unit,
        onBalanceViewChange: () -> Unit,
        onShare: () -> Unit,
        onRate: () -> Unit,
        onPrivacyPolicy: () -> Unit,
        onContactUs: () -> Unit,
    ) {

        when (item.title) {
            R.string.currency_and_format -> onCurrencyChange(currCountryCode)
            R.string.default_payment_mode -> onDefaultPaymentChange(currPaymentId)
            R.string.home_page_balance_view -> onBalanceViewChange()
            R.string.share -> onShare()
            R.string.rate -> onRate()
            R.string.privacy_policy -> onPrivacyPolicy()
            R.string.contact_us -> onContactUs()
        }

    }
}