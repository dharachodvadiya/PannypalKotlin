package com.indie.apps.pennypal.presentation.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.domain.usecase.GetGeneralSettingUseCase
import com.indie.apps.pennypal.repository.AuthRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.SettingEffect
import com.indie.apps.pennypal.util.SettingOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getGeneralSettingUseCase: GetGeneralSettingUseCase,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
   // private val backupRepository: BackupRepository
) : ViewModel() {

    //private var isInProcess = false

    var userState = userRepository.getUser()
        .map { it.copy(email = if (authRepository.isSignedIn()) authRepository.getUserInfo()?.email else null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    //val syncEffect = MutableSharedFlow<SyncEffect?>(replay = 0)
    val settingEffect = MutableSharedFlow<SettingEffect>(replay = 0)

    //val processingState = MutableStateFlow(AuthProcess.NONE)

    var generalList = getGeneralSettingUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val moreList = MutableStateFlow(
        listOf(
            MoreItem(R.string.share, SettingOption.SHARE, R.drawable.ic_share),
            MoreItem(R.string.rate, SettingOption.RATE, R.drawable.ic_rate),
            MoreItem(R.string.privacy_policy, SettingOption.PRIVACY_POLICY, R.drawable.ic_privacy),
            MoreItem(R.string.contact_us, SettingOption.CONTACT_US, R.drawable.ic_contact_us),
        )
    )

    val backupRestoreList = MutableStateFlow(
        listOf(
            MoreItem(R.string.backup_Data, SettingOption.BACKUP, R.drawable.ic_backup),
            MoreItem(R.string.restore_Data, SettingOption.RESTORE, R.drawable.ic_restore),
        )
    )

    fun onSelectOption(item: MoreItem) {
        viewModelScope.launch {
            settingEffect.emit(
                when (item.option) {
                    SettingOption.CURRENCY_CHANGE -> SettingEffect.OnCurrencyChange(item.id)
                    SettingOption.PAYMENT_CHANGE -> SettingEffect.OnDefaultPaymentChange(item.id.toLong())
                    SettingOption.BALANCE_VIEW_CHANGE -> SettingEffect.OnBalanceViewChange
                    SettingOption.SHARE -> SettingEffect.Share
                    SettingOption.RATE -> SettingEffect.Rate
                    SettingOption.PRIVACY_POLICY -> SettingEffect.PrivacyPolicy
                    SettingOption.CONTACT_US -> SettingEffect.ContactUs
                    SettingOption.BACKUP -> {
                        updateSyncTime()
                        SettingEffect.Backup
                    }
                    SettingOption.RESTORE -> SettingEffect.Restore
                    SettingOption.GOOGLE_SIGN_IN -> SettingEffect.GoogleSignIn
                }
            )
        }

    }

    fun updateSyncTime(){
        viewModelScope.launch {
            userRepository.updateLastSyncTime(System.currentTimeMillis())
        }
    }

    fun refreshState() {
        userState = userRepository.getUser()
            .map { it.copy(email = if (authRepository.isSignedIn()) authRepository.getUserInfo()?.email else null) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

        generalList = getGeneralSettingUseCase.loadData()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
    }
}

