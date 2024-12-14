package com.indie.apps.pennypal.presentation.ui.screen

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.repository.AuthRepository
import com.indie.apps.pennypal.repository.BackupRepository
import com.indie.apps.pennypal.util.AuthProcess
import com.indie.apps.pennypal.util.SyncCallBackEvent
import com.indie.apps.pennypal.util.SyncEffect
import com.indie.apps.pennypal.util.SyncEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository, private val backupRepository: BackupRepository
) : ViewModel() {

    private var isInProcess = false
    val syncEffect = MutableSharedFlow<SyncEffect?>(replay = 0)
    val syncCallBackEvent = MutableSharedFlow<SyncCallBackEvent?>(replay = 0)
    val processingState = MutableStateFlow(AuthProcess.NONE)

    fun onEvent(
        mainEvent: SyncEvent,
    ) {
        println("aaaaa onEvent $mainEvent")
        viewModelScope.launch((Dispatchers.IO)) {
            when (mainEvent) {
                SyncEvent.SignInGoogle -> handleSignInGoogle()

                is SyncEvent.OnSignInResult -> handleSignInResult(intent = mainEvent.intent)

                is SyncEvent.OnAuthorizeResult -> handleAuthorizeResult(intent = mainEvent.intent)

                SyncEvent.SignOut -> authRepository.signOut()

                SyncEvent.Backup -> handleBackup()

                is SyncEvent.Restore -> handleRestore()

                SyncEvent.GetFiles -> {}
            }
        }
    }

    private suspend fun handleSignInGoogle() {
        println("aaaaa handleSignInGoogle")
        if (!authRepository.isSignedIn() && !isInProcess) {
            isInProcess = true
            val getGoogleSignIn = authRepository.signInGoogle()
            syncEffect.emit(SyncEffect.SignIn(getGoogleSignIn))
        }
    }

    private suspend fun handleSignInResult(intent: Intent) {
        println("aaaaa handleSignInResult")
        val getResult = authRepository.getSignInResult(intent)
        if (getResult != null) {
            val authorizeGoogleDrive = authRepository.authorizeGoogleDrive()
            if (authorizeGoogleDrive.hasResolution()) {
                syncEffect.emit(
                    SyncEffect.Authorize(authorizeGoogleDrive.pendingIntent!!.intentSender)
                )
            } else {
                isInProcess = false
                //onLoginSuccess(getResult)
                syncCallBackEvent.emit(SyncCallBackEvent.OnLoginSuccess(getResult))
                continueBackUpOrRestoreProcess()
            }
        } else {
            resetProcessState()
            //onLoginFail("Login Fail")
            syncCallBackEvent.emit(
                SyncCallBackEvent.OnLoginFail("Login Fail")
            )
        }
    }

    private suspend fun handleAuthorizeResult(intent: Intent) {
        println("aaaaa handleAuthorizeResult")
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
        println("aaaaa handleBackup")
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
        println("aaaaa handleRestore")
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
        println("aaaaa continueBackUpOrRestoreProcess")
        when (processingState.value) {
            AuthProcess.BACK_UP -> onEvent(SyncEvent.Backup)
            AuthProcess.RESTORE -> onEvent(SyncEvent.Restore)
            AuthProcess.NONE -> {}
        }
    }

    private fun resetProcessState() {
        isInProcess = false
        processingState.value = AuthProcess.NONE
    }
}

