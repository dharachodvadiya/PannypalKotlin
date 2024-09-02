package com.indie.apps.pennypal.presentation.ui.dialog.contact_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.contacts.common.Result
import com.indie.apps.contacts.data.model.ContactNumInfos
import com.indie.apps.contacts.data.repo.ContactsRepository
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.handleException
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
    val uiState = MutableStateFlow<Resource<ContactNumInfos>>(Resource.Loading())

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
                    Resource.Success(result.data ?: Collections.emptyList())
                }
            }

            is Result.Error -> {
                uiState.update { Resource.Error(result.exception?.let { it1 -> handleException(it1).message } + ": ${result.exception?.message}") }
            }
        }
    }

}