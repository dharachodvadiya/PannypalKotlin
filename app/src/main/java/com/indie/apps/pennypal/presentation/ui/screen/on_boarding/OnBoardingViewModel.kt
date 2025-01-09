package com.indie.apps.pennypal.presentation.ui.screen.on_boarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.domain.usecase.UpdateUserCurrencyDataUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateUserNameUseCase
import com.indie.apps.pennypal.presentation.ui.component.UiText
import com.indie.apps.pennypal.presentation.ui.navigation.OnBoardingPage
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.AppLanguage
import com.indie.apps.pennypal.util.ErrorMessage
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    userRepository: UserRepository,
    private val countryRepository: CountryRepository,
    private val updateUserNameUseCase: UpdateUserNameUseCase,
    private val updateUserCurrencyDataUseCase: UpdateUserCurrencyDataUseCase,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val userData = userRepository.getUser()
        .onEach { user ->
            if (user != null) {
                if (currencyCountryCode.value.isEmpty()) setCountryCode(user.currencyCountryCode)
                updateNameText(user.name)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    val currentPageState = MutableStateFlow(OnBoardingPage.BEGIN)
    val currencyCountryCode = MutableStateFlow("")
    val currencyText = MutableStateFlow("")
    val nameState = MutableStateFlow(TextFieldState())

    val introDataList = listOf(
        IntroData(R.string.introTitle1, R.string.introSubTitle1, R.drawable.grow),
        IntroData(R.string.introTitle2, R.string.introSubTitle2, R.drawable.cross),
        IntroData(R.string.introTitle3, R.string.introSubTitle3, R.drawable.resource_private)
    )

    val languageList = MutableStateFlow<List<AppLanguage>>(AppLanguage.entries)

    val currentLanguageIndex =
        MutableStateFlow(preferenceRepository.getInt(Util.PREF_APP_LANGUAGE, 1))

    fun onLanguageSelect(option: AppLanguage, onSuccess: (UiText) -> Unit) {
        preferenceRepository.putInt(Util.PREF_APP_LANGUAGE, option.index)
        currentLanguageIndex.value = option.index
        onSuccess(option.languageCode)
    }

    init {
        viewModelScope.launch { userData.collect() }
    }

    fun onContinueClick(
        currentPage: OnBoardingPage,
        onBoardingComplete: () -> Unit,
        isBackUpAvailable: Boolean = false
    ) {
        when (currentPage) {
            OnBoardingPage.BEGIN -> currentPageState.value = OnBoardingPage.INTRO
            OnBoardingPage.INTRO -> currentPageState.value = OnBoardingPage.SET_LANGUAGE
            OnBoardingPage.SET_LANGUAGE -> {
                saveName { currentPageState.value = OnBoardingPage.SET_NAME }
            }

            OnBoardingPage.SET_NAME -> {
                saveName { currentPageState.value = OnBoardingPage.SET_CURRENCY }
            }

            OnBoardingPage.SET_CURRENCY -> {
                saveCurrency {
                    currentPageState.value = OnBoardingPage.WELCOME
                    preferenceRepository.putBoolean(Util.PREF_NEW_INSTALL, false)
                }
            }

            OnBoardingPage.WELCOME -> if (isBackUpAvailable) currentPageState.value =
                OnBoardingPage.RESTORE else onBoardingComplete()

            OnBoardingPage.RESTORE -> onBoardingComplete()
        }
    }

    fun isLoginClick(currentPage: OnBoardingPage) = currentPage == OnBoardingPage.WELCOME
    fun isRestoreClick(currentPage: OnBoardingPage) = currentPage == OnBoardingPage.RESTORE

    fun onBackClick(currentPage: OnBoardingPage) {
        when (currentPage) {
            OnBoardingPage.BEGIN -> {}
            OnBoardingPage.INTRO -> currentPageState.value = OnBoardingPage.BEGIN
            OnBoardingPage.SET_LANGUAGE -> currentPageState.value = OnBoardingPage.INTRO
            OnBoardingPage.SET_NAME -> currentPageState.value = OnBoardingPage.SET_LANGUAGE
            OnBoardingPage.SET_CURRENCY -> currentPageState.value = OnBoardingPage.SET_NAME
            OnBoardingPage.WELCOME -> currentPageState.value = OnBoardingPage.SET_CURRENCY
            OnBoardingPage.RESTORE -> {}
        }
    }

    fun setCountryCode(code: String) {
        currencyCountryCode.value = code
        currencyText.value = getCurrencyText()
    }

    fun getDefaultCurrencyCode() =
        countryRepository.getDefaultCountryCode()

    private fun getCurrencyText(): String {
        val currencyCode =
            countryRepository.getCurrencyCodeFromCountryCode(currencyCountryCode.value)
        val currencySymbol = countryRepository.getSymbolFromCurrencyCode(currencyCode)
        return "$currencyCode ($currencySymbol)"
    }

    fun updateNameText(text: String) {
        nameState.value.updateText(text)
    }

    private fun saveName(onSuccess: () -> Unit) {
        if (nameState.value.text.isEmpty()) {
            nameState.value.setError(ErrorMessage.USER_NAME_EMPTY)
            return
        }

        viewModelScope.launch {
            updateUserNameUseCase
                .updateData(
                    name = nameState.value.text
                )
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }
    }

    private fun saveCurrency(onSuccess: () -> Unit) {
        viewModelScope.launch {
            updateUserCurrencyDataUseCase
                .updateData(
                    currency = countryRepository.getCurrencyCodeFromCountryCode(currencyCountryCode.value),
                    currencyCountryCode = currencyCountryCode.value
                )
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }
    }

}

data class IntroData(
    @StringRes val title: Int,
    @StringRes val subTitle: Int,
    @DrawableRes val imageId: Int
)