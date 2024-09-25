package com.indie.apps.pennypal.presentation.ui.dialog.select_category

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogSelectCategory(
    selectCategoryViewModel: SelectCategoryViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    selectedId: Long,
    type: Int = -1,
    onSelect: (Category) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    LaunchedEffect(type) {
        selectCategoryViewModel.setType(type)
    }

    val categoryList by selectCategoryViewModel.categoryList.collectAsStateWithLifecycle()
    val currentId by remember {
        mutableLongStateOf(selectedId)
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
            selectedId = 1L
        )
    }
}