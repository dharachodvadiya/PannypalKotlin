package com.indie.apps.pennypal.presentation.ui.screen.setting

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomProgressDialog
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.ads.AdViewModel
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.auth.AuthViewModel
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.auth.SignInLauncher
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel.ExportFilterDialog
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel.ExportSaveDialogs
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel.ExportViewModel
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.app_enum.AuthProcess
import com.indie.apps.pennypal.util.app_enum.DialogType
import com.indie.apps.pennypal.util.app_enum.ExportType
import com.indie.apps.pennypal.util.app_enum.SettingEffect
import com.indie.apps.pennypal.util.app_enum.SyncEvent

@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel(),
    pdfExportViewModel: ExportViewModel = hiltViewModel(),
    onCurrencyChange: (String) -> Unit,
    onDefaultPaymentChange: (Long) -> Unit,
    onBalanceViewChange: () -> Unit,
    onLanguageChange: () -> Unit,
    onTransaction: () -> Unit,
    onMerchants: () -> Unit,
    onCategories: () -> Unit,
    onBudget: () -> Unit,
    onPurchase: () -> Unit,
    onNavigationUp: () -> Unit,
    bottomPadding: PaddingValues,
) {
    val context = LocalContext.current

    // Load ad when screen is created
    LaunchedEffect(Unit) {
        adViewModel.loadInterstitialAd()
    }
    val screenList by settingViewModel.screenList.collectAsStateWithLifecycle()
    val premiumList by settingViewModel.premiumList.collectAsStateWithLifecycle()
    val generalList by settingViewModel.generalList.collectAsStateWithLifecycle()
    val languageList by settingViewModel.languageList.collectAsStateWithLifecycle()
    val moreList by settingViewModel.moreList.collectAsStateWithLifecycle()
    val backupRestoreList by settingViewModel.backupRestoreList.collectAsStateWithLifecycle()
    val userState by settingViewModel.userState.collectAsStateWithLifecycle()
    var openDialog by remember { mutableStateOf<DialogType?>(null) }
    var exportType by remember { mutableStateOf<ExportType?>(null) }

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

    val pdfExportResult by pdfExportViewModel.exportResult.collectAsStateWithLifecycle()

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openDialog = DialogType.ExportFilter
        }
    }

    // Permission handling
    val requestPermission = { action: () -> Unit ->
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            action() // No permission needed for API 29+ (scoped storage)
        }
    }

    BackHandler {
        onNavigationUp()
    }

    SignInLauncher(
        authViewModel,
        onLoginSuccess = {
            settingViewModel.logEvent("setting_login_success")
            settingViewModel.refreshState()
            context.showToast(loginSuccessMessage)
        },
        onRestoreSuccess = {
            settingViewModel.logEvent("setting_restore_success")
            adViewModel.showInterstitialAd(context as Activity, isReload = true) {
                settingViewModel.refreshState()
                context.showToast(restoreSuccessMessage)
            }
        },
        onBackUpSuccess = {
            settingViewModel.logEvent("setting_backup_success")
            adViewModel.showInterstitialAd(context as Activity, isReload = true) {
                context.showToast(backupSuccessMessage)
            }
        },
        onRestoreFail = {
            settingViewModel.logEvent("setting_restore_fail")
            context.showToast(it)
        },
        onBackUpFail = {
            settingViewModel.logEvent("setting_backup_fail")
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

                SettingEffect.Purchase -> {
                    onPurchase()
                }

                SettingEffect.Share -> onShareClick(context)
                SettingEffect.Rate -> onRateClick(context)
                SettingEffect.PrivacyPolicy -> onPrivacyPolicyClick(context)
                SettingEffect.ContactUs -> onContactUsClick(context)

                SettingEffect.Backup -> authViewModel.onEvent(
                    mainEvent = SyncEvent.Backup,
                    onFail = {
                        context.showToast(it)
                    }
                )

                SettingEffect.GoogleSignIn -> authViewModel.onEvent(
                    mainEvent = SyncEvent.SignInGoogle,
                    onFail = {
                        context.showToast(it)
                    }
                )

                SettingEffect.Restore -> authViewModel.onEvent(
                    mainEvent = SyncEvent.Restore,
                    onFail = {
                        context.showToast(it)
                    }
                )

                SettingEffect.GoogleSignInOrChange -> authViewModel.onEvent(
                    mainEvent = SyncEvent.SignInGoogleOrChange,
                    onFail = {
                        context.showToast(it)
                    }
                )

                SettingEffect.OnBudgets -> onBudget()
                SettingEffect.OnCategories -> onCategories()
                SettingEffect.OnMerchants -> onMerchants()
                SettingEffect.OnTransactions -> onTransaction()
                SettingEffect.OnPdfExport -> {
                    exportType = ExportType.Pdf
                    requestPermission { openDialog = DialogType.ExportFilter }

                }

                SettingEffect.OnExcelExport -> {
                    exportType = ExportType.Excel
                    requestPermission { openDialog = DialogType.ExportFilter }
                }
            }
        }
    }

    SettingScreenData(
        generalList = generalList,
        screenList = screenList,
        premiumList = premiumList,
        languageList = languageList,
        moreList = moreList,
        backupRestoreList = backupRestoreList,
        onSelect = {
            settingViewModel.logEvent("setting_${it.option}_click")
            settingViewModel.onSelectOption(
                item = it
            )
        },
        onBackup = {
            settingViewModel.logEvent("setting_backup_now")
            settingViewModel.updateSyncTime()
            authViewModel.onEvent(
                mainEvent = SyncEvent.Backup,
                onFail = {
                    context.showToast(it)
                }
            )
        },
        onProfileClick = {
            settingViewModel.logEvent("setting_profile")
            authViewModel.onEvent(
                mainEvent = SyncEvent.SignInGoogle,
                onFail = {
                    context.showToast(it)
                }
            )
        },
        bottomPadding = bottomPadding,
        user = userState,
        adViewModel = adViewModel
    )



    openDialog?.let { dialog ->
        when (dialog) {
            DialogType.ExportFilter -> {
                val id = when (exportType) {
                    ExportType.Pdf -> R.string.select_date_range_6_month_pdf
                    ExportType.Excel -> R.string.select_date_range_6_month_excel
                    null -> R.string.select_date_range_6_month_pdf
                }
                ExportFilterDialog(
                    title = id,
                    onDismiss = {
                        openDialog = null
                    },
                    onExportClick = { fromDate, toDate ->
                        openDialog = null
                        when (exportType) {
                            ExportType.Pdf -> pdfExportViewModel.exportToPdf(
                                context,
                                fromDate,
                                toDate
                            )

                            ExportType.Excel -> pdfExportViewModel.exportToExcel(fromDate, toDate)
                            null -> {}
                        }

                    },
                    logEvent = {
                        settingViewModel.logEvent("setting_export_filter_dialog_$it")
                    }
                )
            }

            else -> {}
        }
    }

    // Processing and result dialogs
    ExportSaveDialogs(
        exportType = exportType,
        exportResult = pdfExportResult,
        onClearResult = { pdfExportViewModel.clearExportResult() },
        logEvent = {
            settingViewModel.logEvent("setting_export_dialog_$it")
        }
    )
}


@Preview
@Composable
private fun SettingScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        /*SettingScreen(
            onDefaultPaymentChange = {},
            onCurrencyChange = {},
            onBalanceViewChange = {},
            bottomPadding = PaddingValues(0.dp),
            onLanguageChange = {},
            onNavigationUp = {}
        )*/
    }
}
