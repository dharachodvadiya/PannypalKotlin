package com.indie.apps.pennypal.presentation.ui.screen.setting

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.User
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomProgressDialog
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.screen.AuthViewModel
import com.indie.apps.pennypal.presentation.ui.screen.SignInLauncher
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.AuthProcess
import com.indie.apps.pennypal.util.SettingEffect
import com.indie.apps.pennypal.util.SyncEvent

@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onCurrencyChange: (String) -> Unit,
    onDefaultPaymentChange: (Long) -> Unit,
    onBalanceViewChange: () -> Unit,
    onLanguageChange: () -> Unit,
    bottomPadding: PaddingValues,
) {

    val generalList by settingViewModel.generalList.collectAsStateWithLifecycle()
    val languageList by settingViewModel.languageList.collectAsStateWithLifecycle()
    val moreList by settingViewModel.moreList.collectAsStateWithLifecycle()
    val backupRestoreList by settingViewModel.backupRestoreList.collectAsStateWithLifecycle()
    val userState by settingViewModel.userState.collectAsStateWithLifecycle()

    val backupSuccessMessage = stringResource(id = R.string.backup_success)
    val restoreSuccessMessage = stringResource(id = R.string.restore_success)
    val loginSuccessMessage = stringResource(id = R.string.signin_success)


    val processingState by authViewModel.processingState.collectAsStateWithLifecycle()

    when (processingState) {
        AuthProcess.BACK_UP -> CustomProgressDialog(R.string.backup_Data)
        AuthProcess.RESTORE -> CustomProgressDialog(R.string.restore_Data)
        AuthProcess.NONE -> {}
        AuthProcess.SIGN_IN -> CustomProgressDialog(R.string.sign_in)
    }

    val context = LocalContext.current

    SignInLauncher(authViewModel,
        onLoginSuccess = {
            settingViewModel.refreshState()
            context.showToast(loginSuccessMessage)
        },
        onRestoreSuccess = {
            settingViewModel.refreshState()
            context.showToast(restoreSuccessMessage)
        },
        onBackUpSuccess = {
            context.showToast(backupSuccessMessage)
        },
        onRestoreFail = {
            context.showToast(it)
        },
        onBackUpFail = {
            context.showToast(it)
        })

    LaunchedEffect(key1 = settingViewModel.settingEffect) {
        settingViewModel.settingEffect.collect { settingEffect ->
            when (settingEffect) {

                is SettingEffect.OnCurrencyChange -> onCurrencyChange(settingEffect.countryCode)
                is SettingEffect.OnDefaultPaymentChange -> onDefaultPaymentChange(settingEffect.id)
                SettingEffect.OnBalanceViewChange -> onBalanceViewChange()
                SettingEffect.OnLanguageChange -> {
                    onLanguageChange()
                }

                SettingEffect.Share -> onShareClick(context)
                SettingEffect.Rate -> onRateClick(context)
                SettingEffect.PrivacyPolicy -> onPrivacyPolicyClick(context)
                SettingEffect.ContactUs -> onContactUsClick(context)

                SettingEffect.Backup -> authViewModel.onEvent(
                    mainEvent = SyncEvent.Backup,
                )

                SettingEffect.GoogleSignIn -> authViewModel.onEvent(
                    mainEvent = SyncEvent.SignInGoogle,
                )

                SettingEffect.Restore -> authViewModel.onEvent(
                    mainEvent = SyncEvent.Restore,
                )

                SettingEffect.GoogleSignInOrChange -> authViewModel.onEvent(
                    mainEvent = SyncEvent.SignInGoogleOrChange,
                )
            }
        }
    }

    SettingScreenData(
        generalList = generalList,
        languageList = languageList,
        moreList = moreList,
        backupRestoreList = backupRestoreList,
        onSelect = {
            settingViewModel.onSelectOption(
                item = it
            )
        },
        onBackup = {
            settingViewModel.updateSyncTime()
            authViewModel.onEvent(
                mainEvent = SyncEvent.Backup,
            )
        },
        onProfileClick = {
            authViewModel.onEvent(
                mainEvent = SyncEvent.SignInGoogle,
            )
        },
        bottomPadding = bottomPadding,
        user = userState
    )
}

@Composable
fun SettingScreenData(
    generalList: List<MoreItem>,
    languageList: List<MoreItem>,
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
                titleId = R.string.language_support,
                dataList = languageList,
                onSelect = onSelect,
                arrowIconEnable = true
            )


            SettingTypeItem(
                titleId = R.string.backup_restore,
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
            bottomPadding = PaddingValues(0.dp),
            onLanguageChange = {}
        )
    }
}
