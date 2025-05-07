package com.indie.apps.pennypal.util.app_enum

import android.content.Intent
import android.content.IntentSender
import androidx.credentials.Credential
import androidx.credentials.GetCredentialRequest
import com.indie.apps.pennypal.data.module.user.UserInfoResult

sealed class SyncEffect {
    data class SignIn(val getCredentialRequest: GetCredentialRequest) : SyncEffect()
    data class Authorize(val intentSender: IntentSender) : SyncEffect()
}

sealed class SyncEvent {
    data object SignInGoogle : SyncEvent()
    data object SignInGoogleOrChange : SyncEvent()
    data object SignOut : SyncEvent()

    data object Backup : SyncEvent()

    data class OnSignInResult(
        val credential: Credential? = null,
        val errorMessage: String? = null
    ) : SyncEvent()

    data class OnAuthorizeResult(val intent: Intent) : SyncEvent()
    data object Restore : SyncEvent()

    data object GetFiles : SyncEvent()
}

sealed class SyncCallBackEvent {
    data class OnLoginSuccess(val userInfo: UserInfoResult?) : SyncCallBackEvent()
    data class OnLoggedIn(val userInfo: UserInfoResult?) : SyncCallBackEvent()
    data class OnLoginFail(val message: String) : SyncCallBackEvent()
    data object OnBackUpSuccess : SyncCallBackEvent()
    data class OnBackUpFail(val message: String) : SyncCallBackEvent()
    data object OnRestoreSuccess : SyncCallBackEvent()
    data class OnRestoreFail(val message: String) : SyncCallBackEvent()
}

enum class AuthProcess {
    BACK_UP,
    RESTORE,
    SIGN_IN,
    NONE
}