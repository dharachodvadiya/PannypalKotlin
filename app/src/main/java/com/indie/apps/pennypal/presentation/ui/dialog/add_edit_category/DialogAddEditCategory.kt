import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.enum.CategoryType
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.dialog.add_edit_category.AddEditCategoryDialogField
import com.indie.apps.pennypal.presentation.ui.dialog.add_edit_category.AddEditCategoryViewModel
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun DialogAddEditCategory(
    viewModel: AddEditCategoryViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSaveSuccess: (Category?, Boolean) -> Unit,
    categoryType: Int?,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val enableButton by viewModel.enableButton.collectAsStateWithLifecycle()
    val categoryState by viewModel.categoryState.collectAsStateWithLifecycle()

    val selectedCategoryId by viewModel.selectedCategoryId.collectAsStateWithLifecycle()
    val selectedCategoryIconColor by viewModel.selectedCategoryIconColor.collectAsStateWithLifecycle()
    val selectedCategoryIcon by viewModel.selectedCategoryIcon.collectAsStateWithLifecycle()

    if (categoryType != null) {
        LaunchedEffect(categoryType) {
            viewModel.setSelectedCategoryId(categoryType)
        }
    }

    val context = LocalContext.current
    val categorySaveToast = stringResource(id = R.string.category_save_success_toast)
    val categoryEditToast = stringResource(id = R.string.category_edit_success_toast)

    MyAppDialog(
        title = if (!viewModel.getIsEditable()) R.string.add_category else R.string.edit_category,
        onNavigationUp = {
            if (enableButton)
                onNavigationUp()
        },
        isFixHeight = true,
        content = {
            AddEditCategoryDialogField(
                textCategory = categoryState,
                list = CategoryType.entries,
                selectCategoryType = CategoryType.entries.first { it.id == selectedCategoryId },
                onSelectCategoryType = viewModel::onModeChange,
                onCategoryNameTextChange = viewModel::updateCategoryTypeText,
                onSelectCategoryIcon = viewModel::onCategoryIconChange,
                onSelectCategoryIconColor = viewModel::onCategoryIconColorChange,
                selectedIcon = selectedCategoryIcon,
                selectedIconColor = selectedCategoryIconColor
            )
        },
        bottomContent = {
            BottomSaveButton(
                onClick = {
                    viewModel.addEditCategory { category, isEdit ->
                        onSaveSuccess(category, isEdit)
                        context.showToast(if (isEdit) categoryEditToast else categorySaveToast)
                    }
                },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
            )
        },
        modifier = modifier
    )
    //}
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogAddEditCategory(
            onNavigationUp = {},
            onSaveSuccess = { _, _ -> },
            categoryType = null
        )
    }
}