package com.indie.apps.pannypal.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pannypal.data.module.MerchantDataWithName
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.domain.usecase.GetMerchantNameAndDetailListUseCase
import com.indie.apps.pannypal.domain.usecase.SearchMerchantNameAndDetailListUseCase
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.util.Constant
import com.indie.apps.pannypal.util.Resource
import com.indie.apps.pannypal.util.handleException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMerchantViewModel @Inject constructor(
    private val getMerchantListUseCase: GetMerchantNameAndDetailListUseCase,
    private val searchMerchantNameAndDetailListUseCase: SearchMerchantNameAndDetailListUseCase
) : ViewModel() {

    val searchTextState by mutableStateOf(TextFieldState())

    private var pageNo = 1
    private var searchPageNo = 1

    private var isMoreDataAvailable = true
    private var isMoreSearchDataAvailable = true

    private var isSearch = false
    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    private val currDataList = arrayListOf<MerchantNameAndDetails>()
    private val currSearchDataList = arrayListOf<MerchantNameAndDetails>()

    val uiState = trigger.flatMapLatest { _ ->
        if (isSearch) {
            if(searchTextState.text.isNullOrEmpty()){

                flow{
                    emit(Resource.Success(data = listOf()))
                }
            }else {
                searchMerchantNameAndDetailListUseCase.loadData(searchTextState.text, searchPageNo)
            }
        } else {
            getMerchantListUseCase.loadData(pageNo)
        }
    }.onEach { resource ->
        if (resource is Resource.Success) {
            val dataSize = resource.data?.size ?: 0
            if (dataSize > 0 && dataSize == Constant.QUERY_PAGE_SIZE ){
            //if (dataSize > 0) {
                if (isSearch) {
                    if(!searchTextState.text.isNullOrEmpty()) searchPageNo++
                } else {
                    pageNo++
                }
            } else {
                if (isSearch) {
                    if(!searchTextState.text.isNullOrEmpty()) isMoreSearchDataAvailable = false
                } else {
                    isMoreDataAvailable = false
                }
            }

            if(isSearch && searchTextState.text.isNullOrEmpty())
                isSearch = false

        }
    }
        .map { resource -> if (resource is Resource.Success) getCurrentDataList(resource.data) else resource }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())


    init {
        loadMoreData()
    }

    private fun getCurrentDataList(newList: List<MerchantNameAndDetails>?): Resource.Success<List<MerchantNameAndDetails>> {
        if (!newList.isNullOrEmpty()) {
            if (isSearch) currSearchDataList.addAll(newList) else currDataList.addAll(newList)
        }

        return Resource.Success(data = if (isSearch) currSearchDataList else currDataList)
    }

    fun loadMoreData() {
        val isLoadData = if (isSearch) isMoreSearchDataAvailable else isMoreDataAvailable
        if (isLoadData) {
            viewModelScope.launch {
                trigger.emit(Unit)
            }
        } else {
            println("aaaa no data available")
        }


    }

    fun searchData(searchString: String) {
        clearSearch()
        if (!searchString.isNullOrEmpty()) {
            isSearch = true
        }
        loadMoreData()
    }

    private fun clearSearch() {
        searchPageNo = 1
        isMoreSearchDataAvailable = true
        //isSearch = false
        currSearchDataList.clear()

    }

}