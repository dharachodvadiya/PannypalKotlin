package com.indie.apps.pennypal.presentation.ui.screen.setting

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.domain.usecase.GetGeneralSettingUseCase
import com.indie.apps.pennypal.repository.AuthRepository
import com.indie.apps.pennypal.repository.BackupRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.AuthProcess
import com.indie.apps.pennypal.util.SettingEffect
import com.indie.apps.pennypal.util.SettingOption
import com.indie.apps.pennypal.util.SyncEffect
import com.indie.apps.pennypal.util.SyncEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val backupRepository: BackupRepository
) : ViewModel() {

    private var isInProcess = false

    var userState = userRepository.getUser()
        .map { it.copy(email = if (authRepository.isSignedIn()) authRepository.getUserInfo()?.email else null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val syncEffect = MutableSharedFlow<SyncEffect?>(replay = 0)
    val settingEffect = MutableSharedFlow<SettingEffect>(replay = 0)

    val processingState = MutableStateFlow(AuthProcess.NONE)

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
            MoreItem(R.string.backup_Data, SettingOption.BACKUP),
            MoreItem(R.string.restore_Data, SettingOption.RESTORE),
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
                    SettingOption.BACKUP -> SettingEffect.Backup
                    SettingOption.RESTORE -> SettingEffect.Restore
                    SettingOption.GOOGLE_SIGN_IN -> SettingEffect.GoogleSignIn
                }
            )
        }

    }

    fun onEvent(mainEvent: SyncEvent) {
        viewModelScope.launch((Dispatchers.IO)) {
            when (mainEvent) {
                SyncEvent.SignInGoogle -> handleSignInGoogle()
                is SyncEvent.OnSignInResult -> handleSignInResult(mainEvent.intent)
                is SyncEvent.OnAuthorizeResult -> handleAuthorizeResult(mainEvent.intent)
                SyncEvent.SignOut -> authRepository.signOut()
                SyncEvent.Backup -> handleBackup()
                is SyncEvent.Restore -> handleRestore()
                SyncEvent.GetFiles -> TODO()
            }
        }
    }


    private suspend fun handleSignInGoogle() {
        if (!authRepository.isSignedIn() && !isInProcess) {
            isInProcess = true
            val getGoogleSignIn = authRepository.signInGoogle()
            syncEffect.emit(SyncEffect.SignIn(getGoogleSignIn))
        }
    }

    private suspend fun handleSignInResult(intent: Intent) {
        isInProcess = false
        val getResult = authRepository.getSignInResult(intent)
        if (getResult != null) {
            val authorizeGoogleDrive = authRepository.authorizeGoogleDrive()
            if (authorizeGoogleDrive.hasResolution()) {
                syncEffect.emit(
                    SyncEffect.Authorize(authorizeGoogleDrive.pendingIntent!!.intentSender)
                )
            } else {
                continueAuthProcess()
            }
        } else {
            processingState.value = AuthProcess.NONE
        }

    }

    private suspend fun handleAuthorizeResult(intent: Intent) {
        isInProcess = false
        val result = authRepository.authorizeGoogleDriveResult(intent)

        if (result != null) continueAuthProcess() else processingState.value = AuthProcess.NONE
    }

    private suspend fun handleBackup() {
        if (!isInProcess) {
            isInProcess = true
            userRepository.updateLastSyncTime(System.currentTimeMillis())
            processingState.value = AuthProcess.BACK_UP
            if (authRepository.isSignedIn()) {
                backupRepository.backup()
                processingState.value = AuthProcess.NONE
                isInProcess = false
            } else {
                isInProcess = false
                onEvent(SyncEvent.SignInGoogle)
            }
        }
    }

    private suspend fun handleRestore() {
        if (!isInProcess) {
            isInProcess = true
            processingState.value = AuthProcess.RESTORE
            if (authRepository.isSignedIn()) {
                backupRepository.restore()
                processingState.value = AuthProcess.NONE
                isInProcess = false
                refreshState()
            } else {
                isInProcess = false
                onEvent(SyncEvent.SignInGoogle)
            }
        }
    }

    private fun continueAuthProcess() {
        when (processingState.value) {
            AuthProcess.BACK_UP -> onEvent(SyncEvent.Backup)
            AuthProcess.RESTORE -> onEvent(SyncEvent.Restore)
            AuthProcess.NONE -> {}
        }
    }

    private fun refreshState() {
        userState = userRepository.getUser()
            .map { it.copy(email = if (authRepository.isSignedIn()) authRepository.getUserInfo()?.email else null) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

        generalList = getGeneralSettingUseCase.loadData()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
    }
}

