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
    data class OnAuthorize(val intent: Intent) : SyncEvent()
    data class Restore(val fileId: String) : SyncEvent()

    data object GetFiles : SyncEvent()

}