package com.indie.apps.pennypal.presentation.ui.dialog.contact_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.contacts.common.Result
import com.indie.apps.contacts.data.model.ContactNumInfos
import com.indie.apps.contacts.data.repo.ContactsRepository
import com.indie.apps.pennypal.data.module.ContactNumberAndCode
import com.indie.apps.pennypal.data.module.ContactNumberAndName
import com.indie.apps.pennypal.data.module.toContactNumberAndCode
import com.indie.apps.pennypal.data.module.toContactNumberAndName
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.Resource
import com.indie.apps.pennypal.util.app_enum.handleException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class ContactPickerViewModel @Inject constructor(private val contactsRepository: ContactsRepository) :
    ViewModel() {


    val searchTextState = MutableStateFlow(TextFieldState())
    val uiState = MutableStateFlow<Resource<List<ContactNumberAndName>>>(Resource.Loading())

    init {
        searchData()
    }


    fun searchData() {
        viewModelScope.launch {
            contactsRepository
                .searchContactsNameWithPhone(searchTextState.value.text)
                .collect { result ->
                    processResult(result)
                }
        }
    }

    private fun processResult(result: Result<ContactNumInfos>) {
        when (result) {
            is Result.Loading -> uiState.update { Resource.Loading() }
            is Result.Success -> {
                uiState.update {
                    Resource.Success(result.data?.map { it.toContactNumberAndName() }
                        ?: Collections.emptyList())
                }
            }

            is Result.Error -> {
                uiState.update { Resource.Error(result.exception?.let { it1 -> handleException(it1).message } + ": ${result.exception?.message}") }
            }
        }
    }

    fun onSelectCountry(contact: ContactNumberAndName, onSuccess: (ContactNumberAndCode) -> Unit) {
        val data =
            Util.getContactNumberAndCodeFromPhoneNumber(contact.phoneNumbers[contact.currentNumberIndex.value])
        onSuccess(contact.toContactNumberAndCode(data.phoneNumber, data.dialCode))
    }

    fun updateSearchText(text: String) = searchTextState.value.updateText(text)


}