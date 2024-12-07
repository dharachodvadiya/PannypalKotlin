package com.indie.apps.pennypal.util

import android.content.Intent

sealed class SyncEffect {
    data class SignIn(val intent: Intent) : SyncEffect()
}

sealed class SyncEvent {
    data object SignInGoogle : SyncEvent()
    data object SignOut : SyncEvent()
    data class OnSignInResult(val intent: Intent) : SyncEvent()

}