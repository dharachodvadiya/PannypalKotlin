package com.indie.apps.pennypal.presentation.ui.screen

import android.content.Intent
import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.repository.AuthRepository
import com.indie.apps.pennypal.repository.BackupRepository
import com.indie.apps.pennypal.repository.NetworkRepository
import com.indie.apps.pennypal.util.app_enum.AppError
import com.indie.apps.pennypal.util.app_enum.AuthProcess
import com.indie.apps.pennypal.util.app_enum.SyncCallBackEvent
import com.indie.apps.pennypal.util.app_enum.SyncEffect
import com.indie.apps.pennypal.util.app_enum.SyncEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val backupRepository: BackupRepository,
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private var isInProcess = false
    val syncEffect = MutableSharedFlow<SyncEffect?>(replay = 0)
    val syncCallBackEvent = MutableSharedFlow<SyncCallBackEvent?>(replay = 0)
    val processingState = MutableStateFlow(AuthProcess.NONE)

    fun isBackupAvailable(callBack: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isAvailable = backupRepository.isBackupAvailable()
            withContext(Dispatchers.Main) {
                callBack(isAvailable)
            }
        }
    }

    fun onEvent(
        mainEvent: SyncEvent,
        onFail: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            when (mainEvent) {
                SyncEvent.SignInGoogle -> {
                    if (!networkRepository.isNetworkAvailable()) {
                        onFail(AppError.NetworkError.message)
                        return@launch
                    }
                    handleSignInGoogle()
                }

                is SyncEvent.OnSignInResult -> handleSignInResult(
                    mainEvent.credential,
                    mainEvent.errorMessage
                )

                is SyncEvent.OnAuthorizeResult -> handleAuthorizeResult(intent = mainEvent.intent)

                SyncEvent.SignOut -> authRepository.signOut()

                SyncEvent.Backup -> {
                    if (!networkRepository.isNetworkAvailable()) {
                        onFail(AppError.NetworkError.message)
                        return@launch
                    }
                    handleBackup()
                }

                is SyncEvent.Restore -> {
                    if (!networkRepository.isNetworkAvailable()) {
                        onFail(AppError.NetworkError.message)
                        return@launch
                    }
                    handleRestore()
                }

                SyncEvent.GetFiles -> {}
                SyncEvent.SignInGoogleOrChange -> {
                    if (!networkRepository.isNetworkAvailable()) {
                        onFail(AppError.NetworkError.message)
                        return@launch
                    }
                    handleSignInGoogle(true)
                }
            }
        }
    }

    private suspend fun handleSignInGoogle(isChangeAccount: Boolean = false) {
        if (!isInProcess) {

            if (isChangeAccount && authRepository.isSignedIn()) {
                authRepository.signOut()
            }
            if (!authRepository.isSignedIn()) {
                if (processingState.value == AuthProcess.NONE)
                    processingState.value = AuthProcess.SIGN_IN
                isInProcess = true
                val getGoogleSignIn = authRepository.signInGoogle()
                if (getGoogleSignIn != null) {
                    syncEffect.emit(SyncEffect.SignIn(getGoogleSignIn))
                } else {
                    resetProcessState()
                }
            } else {
                syncCallBackEvent.emit(
                    SyncCallBackEvent.OnLoggedIn(
                        authRepository.getUserInfo()
                    )
                )
            }
        }
    }

    private suspend fun handleSignInResult(credential: Credential?, errorMessage: String?) {
        if (credential != null) {
            val getResult = authRepository.getSignInResult(credential)
            if (getResult != null) {
                val authorizeGoogleDrive = authRepository.authorizeGoogleDrive()
                if (authorizeGoogleDrive?.hasResolution() == true) {
                    syncEffect.emit(
                        SyncEffect.Authorize(authorizeGoogleDrive.pendingIntent!!.intentSender)
                    )
                    return
                } else {
                    isInProcess = false
                    //onLoginSuccess(getResult)
                    syncCallBackEvent.emit(SyncCallBackEvent.OnLoginSuccess(getResult))
                    continueBackUpOrRestoreProcess()
                    return
                }
            }
        }

        resetProcessState()
        syncCallBackEvent.emit(
            SyncCallBackEvent.OnLoginFail(errorMessage ?: "Login Fail")
        )
    }

    private suspend fun handleAuthorizeResult(intent: Intent) {
        val result = authRepository.authorizeGoogleDriveResult(intent)

        if (result != null) {
            isInProcess = false
            //onAuthSuccess(authRepository.getUserInfo())
            syncCallBackEvent.emit(
                SyncCallBackEvent.OnLoginSuccess(
                    authRepository.getUserInfo()
                )
            )
            continueBackUpOrRestoreProcess()
        } else {
            resetProcessState()
            //onAuthFail("Authorization Fail")
            syncCallBackEvent.emit(
                SyncCallBackEvent.OnLoginFail(
                    "Authorization Fail"
                )
            )
        }
    }

    private suspend fun handleBackup() {
        if (!isInProcess) {
            isInProcess = true
            processingState.value = AuthProcess.BACK_UP

            if (authRepository.isSignedIn()) {

                try {
                    backupRepository.backup()
                    resetProcessState()
                    //onBackUpSuccess()
                    syncCallBackEvent.emit(SyncCallBackEvent.OnBackUpSuccess)
                } catch (e: Exception) {
                    resetProcessState()
                    //onBackUpFail(e.message ?: "Backup fail")
                    syncCallBackEvent.emit(
                        SyncCallBackEvent.OnBackUpFail(
                            e.message ?: "Backup fail"
                        )
                    )
                }
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
                try {
                    backupRepository.restore()
                    resetProcessState()
                    //onRestoreSuccess()
                    syncCallBackEvent.emit(SyncCallBackEvent.OnRestoreSuccess)
                } catch (e: Exception) {
                    resetProcessState()
                    //onRestoreFail(e.message ?: "Restore fail")
                    syncCallBackEvent.emit(
                        SyncCallBackEvent.OnRestoreFail(
                            e.message ?: "Restore fail"
                        )
                    )
                }
            } else {
                isInProcess = false
                onEvent(SyncEvent.SignInGoogle)
            }
        }
    }

    private fun continueBackUpOrRestoreProcess() {
        when (processingState.value) {
            AuthProcess.BACK_UP -> onEvent(SyncEvent.Backup)
            AuthProcess.RESTORE -> onEvent(SyncEvent.Restore)
            AuthProcess.NONE -> resetProcessState()
            AuthProcess.SIGN_IN -> resetProcessState()
        }
    }

    private fun resetProcessState() {
        isInProcess = false
        processingState.value = AuthProcess.NONE
    }
}

