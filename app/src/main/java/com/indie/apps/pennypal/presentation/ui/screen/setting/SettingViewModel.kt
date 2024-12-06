package com.indie.apps.pennypal.presentation.ui.screen.setting

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MoreItem
import com.indie.apps.pennypal.domain.usecase.GetGeneralSettingUseCase
import com.indie.apps.pennypal.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    getGeneralSettingUseCase: GetGeneralSettingUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    val effect = MutableStateFlow<MainEffect?>(null)

    val generalList = getGeneralSettingUseCase
        .loadData()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())
    val moreList = MutableStateFlow(
        listOf(
            MoreItem(title = R.string.share, icon = R.drawable.ic_share),
            MoreItem(title = R.string.rate, icon = R.drawable.ic_rate),
            MoreItem(title = R.string.privacy_policy, icon = R.drawable.ic_privacy),
            MoreItem(title = R.string.contact_us, icon = R.drawable.ic_contact_us),
        )
    )

    val backupRestoreList = MutableStateFlow(
        listOf(
            MoreItem(title = R.string.backup_Data),
            MoreItem(title = R.string.restore_Data),
        )
    )

    fun onSelectOption(
        item: MoreItem,
        onDefaultPaymentChange: (Long) -> Unit,
        onCurrencyChange: (String) -> Unit,
        onBalanceViewChange: () -> Unit,
        onShare: () -> Unit,
        onRate: () -> Unit,
        onPrivacyPolicy: () -> Unit,
        onContactUs: () -> Unit,
        onGoogleSignIn: () -> Unit,
    ) {

        when (item.title) {
            R.string.currency_and_format -> onCurrencyChange(item.id)
            R.string.default_payment_mode -> onDefaultPaymentChange(item.id.toLong())
            R.string.home_page_balance_view -> onBalanceViewChange()
            R.string.share -> onShare()
            R.string.rate -> onRate()
            R.string.privacy_policy -> onPrivacyPolicy()
            R.string.contact_us -> onContactUs()
            R.string.backup_Data -> onGoogleSignIn()
        }

    }

    fun onEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.OnSignInResult -> {
                viewModelScope.launch(Dispatchers.IO) {
                    onSignInResult(mainEvent.intent)
                }
            }

            MainEvent.SignInGoogle -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val getGoogleSignIn = authRepository.signInGoogle()
                    effect.update {
                        MainEffect.SignIn(getGoogleSignIn)
                    }
                }
            }

            MainEvent.SignOut -> {
                viewModelScope.launch(Dispatchers.IO) {
                    authRepository.signOut()

                }
            }
        }
    }

    private suspend fun onSignInResult(intent: Intent) {
        val getResult = authRepository.getSignInResult(intent)
        println("aaaa resule = ${getResult.email}")
    }
}

sealed class MainEffect {
    data class SignIn(val intent: Intent) : MainEffect()
}

sealed class MainEvent {
    data object SignInGoogle : MainEvent()
    data object SignOut : MainEvent()
    data class OnSignInResult(val intent: Intent) : MainEvent()

}