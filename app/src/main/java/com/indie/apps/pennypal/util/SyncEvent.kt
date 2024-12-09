package com.indie.apps.pennypal.util

import android.content.Intent
import android.content.IntentSender

sealed class SyncEffect {
    data class SignIn(val intentSender: IntentSender) : SyncEffect()
    data class Authorize(val intentSender: IntentSender) : SyncEffect()
}

sealed class SyncEvent {
    data object SignInGoogle : SyncEvent()
    data object SignOut : SyncEvent()

    data object Backup : SyncEvent()

    data class OnSignInResult(val intent: Intent) : SyncEvent()
    data class OnAuthorizeResult(val intent: Intent) : SyncEvent()
    data object Restore : SyncEvent()

    data object GetFiles : SyncEvent()
}

enum class AuthProcess {
    BACK_UP,
    RESTORE,
    NONE
}