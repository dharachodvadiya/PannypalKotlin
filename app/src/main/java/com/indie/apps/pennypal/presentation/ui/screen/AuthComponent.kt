package com.indie.apps.pennypal.presentation.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.indie.apps.pennypal.data.module.UserInfoResult
import com.indie.apps.pennypal.util.SyncCallBackEvent
import com.indie.apps.pennypal.util.SyncEffect
import com.indie.apps.pennypal.util.SyncEvent

@Composable
fun SignInLauncher(
    authViewModel: AuthViewModel,
    onLoginSuccess: (UserInfoResult?) -> Unit = {},
    onLoginFail: (String) -> Unit = {},
    onBackUpSuccess: () -> Unit = {},
    onBackUpFail: (String) -> Unit = {},
    onRestoreSuccess: () -> Unit = {},
    onRestoreFail: (String) -> Unit = {}
) {

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            authViewModel.onEvent(
                SyncEvent.OnSignInResult(
                    it.data ?: return@rememberLauncherForActivityResult
                )
            )
        }
    )

    val authorizeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            authViewModel.onEvent(
                SyncEvent.OnAuthorizeResult(
                    it.data ?: return@rememberLauncherForActivityResult
                )
            )
        }
    )

    LaunchedEffect(key1 = authViewModel.syncEffect) {
        authViewModel.syncEffect.collect { effect ->
            when (effect) {
                is SyncEffect.SignIn -> {
                    activityResultLauncher.launch(
                        IntentSenderRequest.Builder(effect.intentSender)
                            .build()
                    )
                }

                is SyncEffect.Authorize -> {
                    authorizeLauncher.launch(
                        IntentSenderRequest.Builder(effect.intentSender)
                            .build()
                    )
                }

                else -> Unit
            }
        }
    }

    LaunchedEffect(key1 = authViewModel.syncCallBackEvent) {
        authViewModel.syncCallBackEvent.collect { effect ->
            when (effect) {
                is SyncCallBackEvent.OnLoginSuccess -> onLoginSuccess(effect.userInfo)

                is SyncCallBackEvent.OnLoginFail -> onLoginFail(effect.message)

                is SyncCallBackEvent.OnRestoreSuccess -> onRestoreSuccess()

                is SyncCallBackEvent.OnRestoreFail -> onRestoreFail(effect.message)

                is SyncCallBackEvent.OnBackUpSuccess -> onBackUpSuccess()

                is SyncCallBackEvent.OnBackUpFail -> onBackUpFail(effect.message)

                else -> Unit
            }
        }
    }
}