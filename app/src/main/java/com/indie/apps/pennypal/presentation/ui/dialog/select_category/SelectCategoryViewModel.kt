package com.indie.apps.pennypal.presentation.ui.dialog.select_category

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.db_entity.Category
import com.indie.apps.pennypal.repository.CategoryRepository
import com.indie.apps.pennypal.util.Util
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

    var addCategoryAnimRun = MutableStateFlow(false)
        private set


    fun setType(type: Int) {
        viewModelScope.launch {
            categoryRepository.getCategoryFromTypeList(type)
                .collect {
                    categoryList.value = it
                }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun addCategorySuccess() {
        addCategoryAnimRun.value = true
        viewModelScope.launch {
            delay(Util.LIST_ITEM_ANIM_DELAY)
            addCategorySuccessAnimStop()
        }
    }

    fun addCategorySuccessAnimStop() {
        if (addCategoryAnimRun.value)
            addCategoryAnimRun.value = false
    }


}