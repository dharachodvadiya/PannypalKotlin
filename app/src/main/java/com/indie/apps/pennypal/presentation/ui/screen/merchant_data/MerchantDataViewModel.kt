package com.indie.apps.pennypal.presentation.ui.screen.merchant_data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.module.MerchantDataWithPaymentName
import com.indie.apps.pennypal.domain.usecase.DeleteMultipleMerchantDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantDataWithPaymentNameListFromMerchantIdUseCase
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.repository.UserRepository
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MerchantDataViewModel @Inject constructor(
    merchantRepository: MerchantRepository,
    //getMerchantDataListFromMerchantId: GetMerchantDataListFromMerchantIdUseCase,
    getMerchantDataWithPaymentNameListFromMerchantIdUseCase: GetMerchantDataWithPaymentNameListFromMerchantIdUseCase,
    private val deleteMultipleMerchantDataUseCase: DeleteMultipleMerchantDataUseCase,
    userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currency = userRepository.getCurrency()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), "$")
    private val merchantId = savedStateHandle.get<String>(Util.PARAM_MERCHANT_ID)?.toLong() ?: 0

    var scrollIndex = MutableStateFlow(0)
    var scrollOffset = MutableStateFlow(0)

    var selectedList = mutableStateListOf<Long>()
        private set

    var isEditable = MutableStateFlow(false)
    var isDeletable = MutableStateFlow(false)

    var editDataAnimRun = MutableStateFlow(false)
    var addDataAnimRun = MutableStateFlow(false)
    var deleteAnimRun = MutableStateFlow(false)
    private lateinit var previousData: PagingData<MerchantDataWithPaymentName>

    val merchantState = merchantRepository.getMerchantFromId(merchantId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

    private val trigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedData = trigger
        .flatMapLatest {
            getMerchantDataWithPaymentNameListFromMerchantIdUseCase.loadData(merchantId)
        }
        .map { item ->

            if (!deleteAnimRun.value) {
                previousData = item
            }
            previousData
        }
        .cachedIn(viewModelScope)

    val pagingState = MutableStateFlow(PagingState<MerchantDataWithPaymentName>())

    init {

        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }

    fun editMerchantDataSuccess() {
        editDataAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            editDataAnimRun.value = false
        }
    }

    fun addMerchantDataSuccess() {
        addDataAnimRun.value = true

        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addMerchantSuccessAnimStop()
        }
    }

    fun addMerchantSuccessAnimStop() {
        if (addDataAnimRun.value)
            addDataAnimRun.value = false
    }

    fun setScrollVal(scrollIndex: Int, scrollOffset: Int) {
        this.scrollIndex.value = scrollIndex
        this.scrollOffset.value = scrollOffset
    }

    fun onItemClick(id: Long) {
        if (!isEditable.value && !isDeletable.value) {
            //callBack(id)
        } else {
            setSelectItem(id)
        }
    }

    fun onItemLongClick(id: Long) {
        setSelectItem(id)
    }

    private fun setSelectItem(id: Long) {
        if (selectedList.contains(id))
            selectedList.remove(id)
        else
            selectedList.add(id)

        changeUpdateState()
    }

    fun clearSelection() {
        selectedList.clear()
        changeUpdateState()
    }

    private fun changeUpdateState() {
        val selectedCount = selectedList.size
        if (selectedCount == 1) {
            isEditable.value = true
            isDeletable.value = true
        } else if (selectedCount > 1) {
            isEditable.value = false
            isDeletable.value = true
        } else {
            isEditable.value = false
            isDeletable.value = false
        }
    }

    fun onDeleteDialogClick(onSuccess: () -> Unit) {
        deleteAnimRun.value = true
        viewModelScope.launch {
            deleteMultipleMerchantDataUseCase
                .deleteData(selectedList)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                            delay(Util.LIST_ITEM_ANIM_DELAY)
                            onDeleteAnimStop()
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }

    private fun onDeleteAnimStop() {
        if (deleteAnimRun.value) {
            deleteAnimRun.value = false
            clearSelection()
            loadData()
        }

    }

    fun onEditClick(onSuccess: (Long) -> Unit) {
        if (selectedList.size > 0) {
            onSuccess(selectedList[0])
            clearSelection()
        }

    }
}