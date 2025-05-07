package com.indie.apps.pennypal.presentation.ui.dialog.multi_select_Category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.common.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DialogMultiSelectCategory(
    multiSelectCategoryViewModel: MultiSelectCategoryViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSave: (List<Long>) -> Unit,
    selectedIds: List<Long>,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    multiSelectCategoryViewModel.setPreSelectedItem(selectedIds)
    val categoryList by multiSelectCategoryViewModel.categoryList.collectAsStateWithLifecycle()
    val selectedList = multiSelectCategoryViewModel.selectedList
    val selectAllState by multiSelectCategoryViewModel.selectAllState.collectAsStateWithLifecycle()
    val searchTextState by multiSelectCategoryViewModel.searchTextState.collectAsStateWithLifecycle()

    var job: Job? = null

    MyAppDialog(isBackEnable = true, title = R.string.select_category, onNavigationUp = {
        onNavigationUp()
    }, content = {
        MultiSelectCategoryDialogField(
            categoryList = categoryList,
            selectedList = selectedList,
            onSelectCategory = {
                multiSelectCategoryViewModel.selectItem(it.id)
            },
            searchTextState = searchTextState,
            onTextChange = {
                multiSelectCategoryViewModel.updateSearchText(it)
                job?.cancel()
                job = MainScope().launch {
                    delay(Util.SEARCH_NEWS_TIME_DELAY)
                    multiSelectCategoryViewModel.searchData()
                }
            },
            isSelectAll = selectAllState,
            onSelectAllCategory = { isSelectAll ->
                multiSelectCategoryViewModel.selectAllClick(isSelectAll)
            })
    }, modifier = modifier, isFixHeight = true, bottomContent = {
        BottomSaveButton(
            onClick = {
                onSave(selectedList)
            }, modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
        )
    })
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogMultiSelectCategory(
            onNavigationUp = {}, onSave = { }, selectedIds = emptyList()
        )
    }
}