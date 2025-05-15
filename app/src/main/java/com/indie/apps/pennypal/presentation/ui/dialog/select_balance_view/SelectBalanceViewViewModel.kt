package com.indie.apps.pennypal.presentation.ui.dialog.select_balance_view

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.indie.apps.pennypal.repository.AnalyticRepository
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.ShowDataPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectBalanceViewViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val analyticRepository: AnalyticRepository
) : ViewModel() {

    val optionList = MutableStateFlow<List<ShowDataPeriod>>(ShowDataPeriod.entries)

    val currentOptionIndex =
        MutableStateFlow(preferenceRepository.getInt(Util.PREF_BALANCE_VIEW, 1))

    fun onSaveOption(option: ShowDataPeriod, onSuccess: () -> Unit) {
        logEvent("balance_view_${option.title}")
        preferenceRepository.putInt(Util.PREF_BALANCE_VIEW, option.index)
        onSuccess()
    }


    fun logEvent(name: String, params: Bundle? = null) {
        analyticRepository.logEvent(name, params)
    }

}