package com.indie.apps.pennypal.presentation.ui.dialog.select_category

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.db_entity.Category
import com.indie.apps.pennypal.repository.CategoryRepository
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.AnimationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    val categoryList = MutableStateFlow<List<Category>>(emptyList())

    val currentAnim = MutableStateFlow(AnimationType.NONE)
    val currentAnimId = MutableStateFlow(-1L)


    fun setType(type: Int) {
        viewModelScope.launch {
            categoryRepository.getCategoryFromTypeList(type)
                .collect {
                    categoryList.value = it
                }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun addCategorySuccess(id: Long) {
        currentAnim.value = AnimationType.ADD
        currentAnimId.value = id
        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addCategorySuccessAnimStop(AnimationType.ADD)
        }
    }

    fun addCategorySuccessAnimStop(animationType: AnimationType) {
        currentAnim.value = AnimationType.NONE
        currentAnimId.value = -1
    }


}