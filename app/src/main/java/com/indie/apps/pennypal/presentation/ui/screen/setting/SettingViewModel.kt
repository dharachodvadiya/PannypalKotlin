package com.indie.apps.pennypal.presentation.ui.screen.setting

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.domain.usecase.GetGeneralSettingUseCase
import com.indie.apps.pennypal.presentation.ui.component.UiText
import com.indie.apps.pennypal.repository.AnalyticRepository
import com.indie.apps.pennypal.repository.AuthRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.AppLanguage
import com.indie.apps.pennypal.util.app_enum.SettingEffect
import com.indie.apps.pennypal.util.app_enum.SettingOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getGeneralSettingUseCase: GetGeneralSettingUseCase,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val preferenceRepository: PreferenceRepository,
    private val analyticRepository: AnalyticRepository,
    // private val backupRepository: BackupRepository
) : ViewModel() {

    val settingEffect = MutableSharedFlow<SettingEffect>(replay = 0)
    private val refreshState = MutableSharedFlow<Unit>(replay = 0)

    var userState = userRepository.getUser()
        .map { it.copy(email = getCurrentEmail()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val screenList = MutableStateFlow(
        listOf(
            MoreItem(R.string.transactions, SettingOption.TRANSACTION, R.drawable.ic_list),
            MoreItem(R.string.merchants, SettingOption.MERCHANT, R.drawable.ic_person_fill),
            MoreItem(R.string.category, SettingOption.CATEGORY, R.drawable.ic_category_fill),
            MoreItem(R.string.budget, SettingOption.BUDGET, R.drawable.ic_budget),
        )
    )

    val premiumList = MutableStateFlow(
        listOf(
            MoreItem(R.string.unlock_premium, SettingOption.PURCHASE, R.drawable.premium),
        )
    )

    var generalList = getGeneralSettingUseCase.loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val languageList =
        preferenceRepository.preferenceChangeListener().onStart { emit(Util.PREF_APP_LANGUAGE) }
            .map {
                getLanguageList()
            }.flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val moreList = MutableStateFlow(
        listOf(
            MoreItem(R.string.share, SettingOption.SHARE, R.drawable.ic_share),
            MoreItem(R.string.rate, SettingOption.RATE, R.drawable.ic_rate),
            MoreItem(R.string.privacy_policy, SettingOption.PRIVACY_POLICY, R.drawable.ic_privacy),
            MoreItem(R.string.contact_us, SettingOption.CONTACT_US, R.drawable.ic_contact_us),
        )
    )

    val backupRestoreList = combine(
        refreshState.onStart { emit(Unit) },
        MutableStateFlow(
            listOf(
                MoreItem(R.string.backup_Data, SettingOption.BACKUP, R.drawable.ic_backup),
                MoreItem(R.string.restore_Data, SettingOption.RESTORE, R.drawable.ic_restore),
                MoreItem(R.string.export_as_pdf, SettingOption.EXPORT_PDF, R.drawable.ic_pdf),
                MoreItem(R.string.export_as_excel, SettingOption.EXPORT_EXCEL, R.drawable.ic_excel),
                MoreItem(
                    R.string.sign_in_change_account,
                    SettingOption.GOOGLE_SIGN_IN_OR_CHANGE,
                    R.drawable.ic_person_fill
                )
            )
        )
    ) { _, list ->
        val updatedList =
            list.toMutableList().map { item ->
                if (item.option == SettingOption.GOOGLE_SIGN_IN_OR_CHANGE) item.copy(subTitle = getCurrentEmail()?.let {
                    UiText.DynamicString(it)
                }) else item
            }
        /*updatedList[4] =
            updatedList[4].copy(subTitle = getCurrentEmail()?.let { UiText.DynamicString(it) })*/
        updatedList
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun onSelectOption(item: MoreItem) {
        viewModelScope.launch {
            settingEffect.emit(
                when (item.option) {
                    SettingOption.CURRENCY_CHANGE -> SettingEffect.OnCurrencyChange(item.id)
                    SettingOption.PAYMENT_CHANGE -> SettingEffect.OnDefaultPaymentChange(item.id.toLong())
                    SettingOption.BALANCE_VIEW_CHANGE -> SettingEffect.OnBalanceViewChange
                    SettingOption.PURCHASE -> SettingEffect.Purchase
                    SettingOption.SHARE -> SettingEffect.Share
                    SettingOption.RATE -> SettingEffect.Rate
                    SettingOption.PRIVACY_POLICY -> SettingEffect.PrivacyPolicy
                    SettingOption.CONTACT_US -> SettingEffect.ContactUs
                    SettingOption.BACKUP -> {
                        updateSyncTime()
                        SettingEffect.Backup
                    }

                    SettingOption.RESTORE -> SettingEffect.Restore
                    SettingOption.GOOGLE_SIGN_IN -> SettingEffect.GoogleSignIn
                    SettingOption.LANGUAGE_CHANGE -> SettingEffect.OnLanguageChange
                    SettingOption.GOOGLE_SIGN_IN_OR_CHANGE -> SettingEffect.GoogleSignInOrChange
                    SettingOption.TRANSACTION -> SettingEffect.OnTransactions
                    SettingOption.MERCHANT -> SettingEffect.OnMerchants
                    SettingOption.CATEGORY -> SettingEffect.OnCategories
                    SettingOption.BUDGET -> SettingEffect.OnBudgets
                    SettingOption.EXPORT_PDF -> SettingEffect.OnPdfExport
                    SettingOption.EXPORT_EXCEL -> SettingEffect.OnExcelExport
                }
            )
        }

    }

    fun updateSyncTime() {
        viewModelScope.launch {
            userRepository.updateLastSyncTime(System.currentTimeMillis())
        }
    }

    fun refreshState() {
        userState = userRepository.getUser()
            .map { it.copy(email = if (authRepository.isSignedIn()) authRepository.getUserInfo()?.email else null) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

        generalList = getGeneralSettingUseCase.loadData()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

        viewModelScope.launch {
            refreshState.emit(Unit)
        }
    }

    private suspend fun getCurrentEmail() =
        if (authRepository.isSignedIn()) authRepository.getUserInfo()?.email else null

    private fun getLanguageList(): List<MoreItem> {
        val language = AppLanguage.fromIndex(
            preferenceRepository.getInt(
                Util.PREF_APP_LANGUAGE, 1
            )
        )?.title
        return listOf(
            MoreItem(
                R.string.current_language,
                SettingOption.LANGUAGE_CHANGE,
                subTitle = language?.let {
                    UiText.StringResource(it)
                }),
        )
    }


    fun logEvent(name: String, params: Bundle? = null) {
        analyticRepository.logEvent(name, params)
    }

}

