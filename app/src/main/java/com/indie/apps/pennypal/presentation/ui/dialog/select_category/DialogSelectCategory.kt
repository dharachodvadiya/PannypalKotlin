package com.indie.apps.pennypal.presentation.ui.dialog.select_category

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.db_entity.Category
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogSelectCategory(
    selectCategoryViewModel: SelectCategoryViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    selectedId: Long,
    type: Int = -1,
    onSelect: (Category) -> Unit,
    onAddCategory: () -> Unit,
    addCategoryId: Long,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    LaunchedEffect(type) {
        selectCategoryViewModel.setType(type)
    }

    val categoryList by selectCategoryViewModel.categoryList.collectAsStateWithLifecycle()
    val currentId by remember {
        mutableLongStateOf(selectedId)
    }

    val addCategoryAnimRun by selectCategoryViewModel.addCategoryAnimRun.collectAsStateWithLifecycle()
    LaunchedEffect(addCategoryId) {
        if (addCategoryId != -1L) {
            selectCategoryViewModel.addCategorySuccess()
        }
    }


    //val paymentState = emptyList<PaymentWithMode>()
    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_category,
        onNavigationUp = {
            onNavigationUp()
        },
        content = {
            SelectCategoryDialogField(
                currentId = currentId,
                categoryList = categoryList,
                onSelectCategory = {
                    onSelect(it)
                },
                onAddCategory = onAddCategory,
                categoryId = addCategoryId,
                addCategoryAnimRun = addCategoryAnimRun,
                onAnimStop = {
                    selectCategoryViewModel.addCategorySuccessAnimStop()
                }
            )
        },
        modifier = modifier,
        isFixHeight = true
    )
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogSelectCategory(
            onNavigationUp = {},
            onSelect = { },
            selectedId = 1L,
            onAddCategory = {},
            addCategoryId = -1,
        )
    }
}