package com.indie.apps.pennypal.presentation.ui.screen.setting

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.db_entity.User
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomProgressDialog
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel.ExportSaveDialogs
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.ads.AdViewModel
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.auth.AuthViewModel
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.iap.BillingViewModel
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel.ExportViewModel
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.auth.SignInLauncher
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel.ExportFilterDialog
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.app_enum.AuthProcess
import com.indie.apps.pennypal.util.app_enum.DialogType
import com.indie.apps.pennypal.util.app_enum.SettingEffect
import com.indie.apps.pennypal.util.app_enum.SyncEvent

@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel(),
    billingViewModel: BillingViewModel = hiltViewModel(),
    pdfExportViewModel: ExportViewModel = hiltViewModel(),
    onCurrencyChange: (String) -> Unit,
    onDefaultPaymentChange: (Long) -> Unit,
    onBalanceViewChange: () -> Unit,
    onLanguageChange: () -> Unit,
    onTransaction: () -> Unit,
    onMerchants: () -> Unit,
    onCategories: () -> Unit,
    onBudget: () -> Unit,
    onNavigationUp: () -> Unit,
    bottomPadding: PaddingValues,
) {
    val context = LocalContext.current

    // Load ad when screen is created
    LaunchedEffect(Unit) {
        adViewModel.loadInterstitialAd()
    }
    val screenList by settingViewModel.screenList.collectAsStateWithLifecycle()
    val generalList by settingViewModel.generalList.collectAsStateWithLifecycle()
    val languageList by settingViewModel.languageList.collectAsStateWithLifecycle()
    val moreList by settingViewModel.moreList.collectAsStateWithLifecycle()
    val backupRestoreList by settingViewModel.backupRestoreList.collectAsStateWithLifecycle()
    val userState by settingViewModel.userState.collectAsStateWithLifecycle()
    var openDialog by remember { mutableStateOf<DialogType?>(null) }

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

    val products by billingViewModel.productDetails.collectAsStateWithLifecycle()

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
    LaunchedEffect(Unit) {
        billingViewModel.init(context as Activity)
    }

    BackHandler {
        onNavigationUp()
    }

    SignInLauncher(
        authViewModel,
        onLoginSuccess = {
            settingViewModel.refreshState()
            context.showToast(loginSuccessMessage)
        },
        onRestoreSuccess = {
            adViewModel.showInterstitialAd(context as Activity, isReload = true) {
                settingViewModel.refreshState()
                context.showToast(restoreSuccessMessage)
            }
        },
        onBackUpSuccess = {
            adViewModel.showInterstitialAd(context as Activity, isReload = true) {
                context.showToast(backupSuccessMessage)
            }
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
            }
        }
    }

    SettingScreenData(
        generalList = generalList,
        screenList = screenList,
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
                onFail = {
                    context.showToast(it)
                }
            )
        },
        onProfileClick ={
            /*authViewModel.onEvent(
                mainEvent = SyncEvent.SignInGoogle,
                onFail = {
                    context.showToast(it)
                }
            )*/

            requestPermission { openDialog = DialogType.ExportFilter }
        },
        bottomPadding = bottomPadding,
        user = userState,
        adViewModel = adViewModel
    )



    openDialog?.let { dialog ->
        when (dialog) {
            DialogType.ExportFilter -> {
                ExportFilterDialog(
                    onDismiss = {
                        openDialog = null
                    },
                    onExportClick = {fromDate,toDate->
                        openDialog = null
                        pdfExportViewModel.exportToPdf(fromDate, toDate)
                    }
                )
            }
            else -> {}
        }
    }

    // Processing and result dialogs
    ExportSaveDialogs(
        pdfExportResult = pdfExportResult,
        onClearResult = { pdfExportViewModel.clearExportResult() }
    )
}

@Composable
fun SettingScreenData(
    screenList: List<MoreItem>,
    generalList: List<MoreItem>,
    languageList: List<MoreItem>,
    moreList: List<MoreItem>,
    backupRestoreList: List<MoreItem>,
    onSelect: (MoreItem) -> Unit,
    onBackup: () -> Unit,
    onProfileClick: () -> Unit,
    bottomPadding: PaddingValues,
    adViewModel: AdViewModel,
    user: User?
) {
    Scaffold { topBarPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(
                    top = topBarPadding.calculateTopPadding(),
                    bottom = bottomPadding.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            val isBannerVisibleFlow = remember { mutableStateOf(false) }
            val bannerAdViewFlow by remember {
                mutableStateOf(
                    adViewModel.loadBannerAd() { adState ->
                        isBannerVisibleFlow.value = adState.bannerAdView != null
                    }
                )
            }


            AnimatedVisibility(
                visible = isBannerVisibleFlow.value,
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg)),
                    factory = { bannerAdViewFlow }
                )
            }

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    // .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                    .padding(horizontal = dimensionResource(id = R.dimen.padding)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
            ) {

                SettingProfileItem(
                    user = user,
                    onClick = onProfileClick,
                    onBackup = onBackup
                )

                SettingItemList(
                    dataList = screenList,
                    onSelect = onSelect,
                    arrowIconEnable = false
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
