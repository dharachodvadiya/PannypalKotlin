package com.indie.apps.pennypal.presentation.ui.dialog.select_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    val categoryList = MutableStateFlow<List<Category>>(emptyList())

    fun setType(type: Int) {
        viewModelScope.launch {
            categoryRepository.getCategoryFromTypeList(type)
                .collect {
                    categoryList.value = it
                }
        }
    }

}