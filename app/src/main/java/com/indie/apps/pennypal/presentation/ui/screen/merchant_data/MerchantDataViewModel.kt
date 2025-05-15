package com.indie.apps.pennypal.presentation.ui.screen.merchant_data

import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithPaymentName
import com.indie.apps.pennypal.domain.usecase.DeleteMerchantDataUseCase
import com.indie.apps.pennypal.domain.usecase.GetMerchantDataWithPaymentNameListFromMerchantIdUseCase
import com.indie.apps.pennypal.presentation.ui.state.PagingState
import com.indie.apps.pennypal.repository.AnalyticRepository
import com.indie.apps.pennypal.repository.MerchantRepository
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.AnimationType
import com.indie.apps.pennypal.util.app_enum.Resource
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
    getMerchantDataWithPaymentNameListFromMerchantIdUseCase: GetMerchantDataWithPaymentNameListFromMerchantIdUseCase,
    private val deleteMultipleMerchantDataUseCase: DeleteMerchantDataUseCase,
    private val analyticRepository: AnalyticRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val merchantId = savedStateHandle.get<String>(Util.PARAM_MERCHANT_ID)?.toLong() ?: 0

    var scrollIndex = MutableStateFlow(0)
    var scrollOffset = MutableStateFlow(0)

    var selectedList = mutableStateListOf<Long>()
        private set

    var isEditable = MutableStateFlow(false)
    var isDeletable = MutableStateFlow(false)

    val currentAnim = MutableStateFlow(AnimationType.NONE)
    val currentAnimId = MutableStateFlow(-1L)
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

            if (currentAnim.value != AnimationType.DELETE) {
                previousData = item
            }
            previousData
        }
        .cachedIn(viewModelScope)

    val pagingState = MutableStateFlow(PagingState<MerchantDataWithPaymentName>())


    fun logEvent(name: String, params: Bundle? = null) {
        analyticRepository.logEvent(name, params)
    }

    init {

        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            trigger.emit(Unit)
        }
    }


    fun setScrollVal(scrollIndex: Int, scrollOffset: Int) {
        this.scrollIndex.value = scrollIndex
        this.scrollOffset.value = scrollOffset
    }

    fun onItemClick(id: Long) {
        if (!isEditable.value && !isDeletable.value) {
            //callBack(id)
            logEvent("merchant_data_item_click")
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


    fun onEditClick(onSuccess: (Long) -> Unit) {
        if (selectedList.size > 0) {
            onSuccess(selectedList[0])
            clearSelection()
        }

    }

    fun onDeleteDialogClick(onSuccess: () -> Unit) {
        currentAnim.value = AnimationType.DELETE
        viewModelScope.launch {
            deleteMultipleMerchantDataUseCase
                .deleteDataList(selectedList)
                .collect {
                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                            delay(Util.LIST_ITEM_ANIM_DELAY)
                            onAnimationComplete(AnimationType.DELETE)
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }

    fun onDeleteFromEditScreenClick(id: Long, onSuccess: () -> Unit) {
        currentAnim.value = AnimationType.DELETE
        currentAnimId.value = id
        viewModelScope.launch {
            deleteMultipleMerchantDataUseCase
                .deleteData(id)
                .collect {

                    when (it) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            onSuccess()
                            delay(Util.LIST_ITEM_ANIM_DELAY)
                            onAnimationComplete(AnimationType.DELETE)

                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }

    }

    fun editMerchantDataSuccess(id: Long) {
        currentAnim.value = AnimationType.EDIT
        currentAnimId.value = id
        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onAnimationComplete(AnimationType.EDIT)
        }
    }

    fun addMerchantDataSuccess(id: Long) {
        currentAnim.value = AnimationType.ADD
        currentAnimId.value = id
        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            onAnimationComplete(AnimationType.ADD)

        }
    }

    fun onAnimationComplete(animationType: AnimationType) {
        currentAnim.value = AnimationType.NONE
        currentAnimId.value = -1L

        when (animationType) {
            AnimationType.DELETE -> {
                clearSelection()
                loadData()
            }

            else -> {}
        }
    }
}