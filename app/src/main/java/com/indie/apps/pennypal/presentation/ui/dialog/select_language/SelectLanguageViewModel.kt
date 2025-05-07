package com.indie.apps.pennypal.presentation.ui.dialog.select_language

import androidx.lifecycle.ViewModel
import com.indie.apps.pennypal.presentation.ui.component.UiText
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectLanguageViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val optionList = MutableStateFlow<List<AppLanguage>>(AppLanguage.entries)

    val currentOptionIndex =
        MutableStateFlow(preferenceRepository.getInt(Util.PREF_APP_LANGUAGE, 1))

    fun onSaveOption(option: AppLanguage, onSuccess: (UiText) -> Unit) {
        preferenceRepository.putInt(Util.PREF_APP_LANGUAGE, option.index)
        onSuccess(option.languageCode)
    }

}