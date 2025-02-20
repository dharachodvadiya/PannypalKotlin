package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.domain.usecase.AddCategoryUseCase
import com.indie.apps.pennypal.domain.usecase.UpdateCategoryUseCase
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.repository.CategoryRepository
import com.indie.apps.pennypal.util.ErrorMessage
import com.indie.apps.pennypal.util.Resource
import com.indie.apps.pennypal.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCategoryViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryEditId =
        savedStateHandle.get<String>(Util.PARAM_CATEGORY_ID)?.toLongOrNull() ?: -1

    val categoryState = MutableStateFlow(TextFieldState())
    val enableButton = MutableStateFlow(true)

    val selectedCategoryId = MutableStateFlow(-1)
    val selectedCategoryIcon = MutableStateFlow(1)
    val selectedCategoryIconColor = MutableStateFlow(1)

    private var editCategory: Category? = null

    init {
        if (categoryEditId != -1L) {
            setEditId(categoryEditId)
        }
    }

    fun setSelectedCategoryId(type: Int) {
        selectedCategoryId.value = type
    }

    private fun setEditId(id: Long) {
        viewModelScope.launch {
            categoryRepository
                .getCategoryFromId(id)
                .collect {
                    editCategory = it

                    setSelectedCategoryId(editCategory!!.type)
                    updateCategoryTypeText(editCategory!!.name)
                    onCategoryIconColorChange(editCategory!!.iconColorId)
                    onCategoryIconChange(editCategory!!.iconId)
                }
        }
    }

    fun getIsEditable() = categoryEditId != -1L

    fun onCategoryIconColorChange(colorId: Int) {
        selectedCategoryIconColor.value = colorId
    }

    fun onCategoryIconChange(iconId: Int) {
        selectedCategoryIcon.value = iconId
    }

    fun addEditCategory(onSuccess: (Category?, Boolean) -> Unit) {

        if (enableButton.value) {
            enableButton.value = false
            if (categoryState.value.text.trim().isEmpty()) {
                categoryState.value.setError(ErrorMessage.CATEGORY_NAME_EMPTY)
                enableButton.value = true
            } else {
                viewModelScope.launch {
                    if (categoryEditId != -1L) {

                        if (editCategory != null) {

                            val category = editCategory!!.copy(
                                id = categoryEditId,
                                name = categoryState.value.text.trim(),
                                type = selectedCategoryId.value,
                                iconId = selectedCategoryIcon.value,
                                iconColorId = selectedCategoryIconColor.value
                            )

                            updateCategoryUseCase
                                .updateData(category)
                                .collect {
                                    when (it) {
                                        is Resource.Loading -> {}
                                        is Resource.Success -> {
                                            onSuccess(category, true)
                                            enableButton.value = true
                                        }

                                        is Resource.Error -> {
                                            categoryState.value.setError(ErrorMessage.CATEGORY_EXIST)
                                            enableButton.value = true
                                        }
                                    }
                                }
                        }

                    } else {
                        val category = Category(
                            name = categoryState.value.text.trim(),
                            type = selectedCategoryId.value,
                            iconId = selectedCategoryIcon.value,
                            iconColorId = selectedCategoryIconColor.value
                        )
                        addCategoryUseCase
                            .addCategory(category)
                            .collect {
                                when (it) {
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        onSuccess(
                                            it.data?.let { it1 ->
                                                category.copy(
                                                    id = it1
                                                )
                                            }, false
                                        )

                                        enableButton.value = true
                                    }

                                    is Resource.Error -> {
                                        categoryState.value.setError(ErrorMessage.CATEGORY_EXIST)
                                        enableButton.value = true
                                    }
                                }
                            }
                    }
                }
            }
        }

    }

    fun updateCategoryTypeText(text: String) = categoryState.value.updateText(text)

}