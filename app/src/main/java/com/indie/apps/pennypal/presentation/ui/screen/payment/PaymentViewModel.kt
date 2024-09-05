package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.lifecycle.ViewModel
import com.indie.apps.cpp.data.repository.CountryRepository
import com.indie.apps.pennypal.domain.usecase.GetUserProfileUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
) : ViewModel(){

    val isInEditMode = MutableStateFlow(false)

    fun setEditMode(isEditable: Boolean)
    {
        isInEditMode.value = isEditable
    }

    fun saveEditedData()
    {
        isInEditMode.value = false
    }

}