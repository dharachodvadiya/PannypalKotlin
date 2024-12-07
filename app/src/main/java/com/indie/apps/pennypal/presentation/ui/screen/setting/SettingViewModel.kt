package com.indie.apps.pennypal.presentation.ui.screen.setting

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.domain.usecase.GetGeneralSettingUseCase
import com.indie.apps.pennypal.repository.AuthRepository
import com.indie.apps.pennypal.util.SettingEffect
import com.indie.apps.pennypal.util.SettingOption
import com.indie.apps.pennypal.util.SyncEffect
import com.indie.apps.pennypal.util.SyncEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    getGeneralSettingUseCase: GetGeneralSettingUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    val syncEffect = MutableStateFlow<SyncEffect?>(null)
    val settingEffect = MutableStateFlow<SettingEffect?>(null)

    val generalList = getGeneralSettingUseCase
        .loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
    val moreList = MutableStateFlow(
        listOf(
            MoreItem(title = R.string.share, icon = R.drawable.ic_share, option =  SettingOption.SHARE),
            MoreItem(title = R.string.rate, icon = R.drawable.ic_rate, option =  SettingOption.RATE),
            MoreItem(title = R.string.privacy_policy, icon = R.drawable.ic_privacy, option =  SettingOption.PRIVACY_POLICY),
            MoreItem(title = R.string.contact_us, icon = R.drawable.ic_contact_us, option =  SettingOption.CONTACT_US),
        )
    )

    val backupRestoreList = MutableStateFlow(
        listOf(
            MoreItem(title = R.string.backup_Data, option =  SettingOption.BACKUP),
            MoreItem(title = R.string.restore_Data, option =  SettingOption.RESTORE),
        )
    )

    fun onSelectOption(
        item: MoreItem,
    ) {
        settingEffect.update {
            when (item.option) {
                SettingOption.CURRENCY_CHANGE -> SettingEffect.OnCurrencyChange(item.id)
                SettingOption.PAYMENT_CHANGE -> SettingEffect.OnDefaultPaymentChange(item.id.toLong())
                SettingOption.BALANCE_VIEW_CHANGE -> SettingEffect.OnBalanceViewChange
                SettingOption.SHARE -> SettingEffect.Share
                SettingOption.RATE -> SettingEffect.Rate
                SettingOption.PRIVACY_POLICY -> SettingEffect.PrivacyPolicy
                SettingOption.CONTACT_US -> SettingEffect.ContactUs
                SettingOption.BACKUP -> SettingEffect.Backup
                SettingOption.RESTORE -> SettingEffect.Restore
            }
        }

    }

    fun onEvent(mainEvent: SyncEvent) {
        when (mainEvent) {
            is SyncEvent.OnSignInResult -> {
                viewModelScope.launch(Dispatchers.IO) {
                    onSignInResult(mainEvent.intent)
                }
            }

            SyncEvent.SignInGoogle -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val getGoogleSignIn = authRepository.signInGoogle()
                    syncEffect.update {
                        SyncEffect.SignIn(getGoogleSignIn)
                    }
                }
            }

            SyncEvent.SignOut -> {
                viewModelScope.launch(Dispatchers.IO) {
                    authRepository.signOut()

                }
            }
        }
    }

    private suspend fun onSignInResult(intent: Intent) {
        val getResult = authRepository.getSignInResult(intent)
        println("aaaa resule = ${getResult.email}")
    }
}

