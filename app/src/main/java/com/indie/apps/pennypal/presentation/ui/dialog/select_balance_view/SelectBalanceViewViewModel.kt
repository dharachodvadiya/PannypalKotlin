package com.indie.apps.pennypal.presentation.ui.dialog.select_balance_view

import androidx.lifecycle.ViewModel
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.ShowDataPeriod
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectBalanceViewViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val optionList = MutableStateFlow<List<ShowDataPeriod>>(ShowDataPeriod.entries)

    val currentOptionIndex =
        MutableStateFlow<Int>(preferenceRepository.getInt(Util.PREF_BALANCE_VIEW, 1))

    fun onSaveOption(option: ShowDataPeriod, onSuccess: () -> Unit) {
        preferenceRepository.putInt(Util.PREF_BALANCE_VIEW, option.index)
        onSuccess()
    }

}