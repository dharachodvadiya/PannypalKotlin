package com.indie.apps.pennypal.presentation.ui.screen.setting

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.User
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomProgressDialog
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.AuthProcess
import com.indie.apps.pennypal.util.SettingEffect
import com.indie.apps.pennypal.util.SyncEffect
import com.indie.apps.pennypal.util.SyncEvent

@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    onCurrencyChange: (String) -> Unit,
    onDefaultPaymentChange: (Long) -> Unit,
    onBalanceViewChange: () -> Unit,
    bottomPadding: PaddingValues,
) {

    val generalList by settingViewModel.generalList.collectAsStateWithLifecycle()
    val moreList by settingViewModel.moreList.collectAsStateWithLifecycle()
    val backupRestoreList by settingViewModel.backupRestoreList.collectAsStateWithLifecycle()
    val userState by settingViewModel.userState.collectAsStateWithLifecycle()


    val processingState by settingViewModel.processingState.collectAsStateWithLifecycle()

    when (processingState) {
        AuthProcess.BACK_UP -> CustomProgressDialog(R.string.backup_Data)
        AuthProcess.RESTORE -> CustomProgressDialog(R.string.restore_Data)
        AuthProcess.NONE -> {}
    }

    val context = LocalContext.current

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            settingViewModel.onEvent(
                SyncEvent.OnSignInResult(
                    it.data ?: return@rememberLauncherForActivityResult
                )
            )
        }
    )

    val authorizeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            settingViewModel.onEvent(
                SyncEvent.OnAuthorizeResult(
                    it.data ?: return@rememberLauncherForActivityResult
                )
            )
        }
    )

    LaunchedEffect(key1 = settingViewModel.syncEffect) {
        settingViewModel.syncEffect.collect { effect ->
            when (effect) {
                is SyncEffect.SignIn -> {
                    /*effect.intent.setRequestCode(Util.REQUEST_CODE_GOOGLE_SIGN_IN)
                    activityResultLauncher.launch(
                        effect.intent
                    )*/
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

    LaunchedEffect(key1 = settingViewModel.settingEffect) {
        settingViewModel.settingEffect.collect { settingEffect ->
            when (settingEffect) {

                is SettingEffect.OnCurrencyChange -> onCurrencyChange(settingEffect.countryCode)
                is SettingEffect.OnDefaultPaymentChange -> onDefaultPaymentChange(settingEffect.id)
                SettingEffect.OnBalanceViewChange -> onBalanceViewChange()

                SettingEffect.Share -> onShareClick(context)
                SettingEffect.Rate -> onRateClick(context)
                SettingEffect.PrivacyPolicy -> onPrivacyPolicyClick(context)
                SettingEffect.ContactUs -> onContactUsClick(context)

                SettingEffect.Backup -> settingViewModel.onEvent(SyncEvent.Backup)
                SettingEffect.Restore -> settingViewModel.onEvent(SyncEvent.Restore)
                SettingEffect.GoogleSignIn -> settingViewModel.onEvent(SyncEvent.SignInGoogle)
            }
        }
    }

    SettingScreenData(
        generalList = generalList,
        moreList = moreList,
        backupRestoreList = backupRestoreList,
        onSelect = {
            settingViewModel.onSelectOption(
                item = it
            )
        },
        onBackup = {
            settingViewModel.onEvent(SyncEvent.Backup)
        },
        onProfileClick = {
            settingViewModel.onEvent(SyncEvent.SignInGoogle)
        },
        bottomPadding = bottomPadding,
        user = userState
    )
}

@Composable
fun SettingScreenData(
    generalList: List<MoreItem>,
    moreList: List<MoreItem>,
    backupRestoreList: List<MoreItem>,
    onSelect: (MoreItem) -> Unit,
    onBackup: () -> Unit,
    onProfileClick: () -> Unit,
    bottomPadding: PaddingValues,
    user: User?
) {
    Scaffold { topBarPadding ->

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(dimensionResource(id = R.dimen.padding))
                .padding(
                    top = topBarPadding.calculateTopPadding(),
                    bottom = bottomPadding.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
        ) {

            SettingProfileItem(
                user = user,
                onClick = onProfileClick,
                onBackup = onBackup
            )

            SettingTypeItem(
                titleId = R.string.general,
                dataList = generalList,
                onSelect = onSelect,
                arrowIconEnable = true
            )

            SettingTypeItem(
                titleId = R.string.more,
                dataList = backupRestoreList,
                onSelect = onSelect,
                arrowIconEnable = false
            )

            SettingTypeItem(
                titleId = R.string.more,
                dataList = moreList,
                onSelect = onSelect,
                arrowIconEnable = false
            )
        }

    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        SettingScreen(
            onDefaultPaymentChange = {},
            onCurrencyChange = {},
            onBalanceViewChange = {},
            bottomPadding = PaddingValues(0.dp)
        )
    }
}
