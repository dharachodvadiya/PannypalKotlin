package com.indie.apps.pennypal.presentation.ui.dialog.multi_select_Category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.presentation.ui.component.DialogSearchView
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.getCategoryColor
import com.indie.apps.pennypal.util.getCategoryIcon

@Composable
fun MultiSelectCategoryDialogField(
    categoryList: List<Category>,
    selectedList: List<Long>,
    isSelectAll: Boolean,
    onSelectCategory: (Category) -> Unit,
    onSelectAllCategory: (Boolean) -> Unit,
    searchTextState: TextFieldState,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        DialogSearchView(
            searchState = searchTextState,
            onTextChange = onTextChange,
        )

        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
                .padding(horizontal = dimensionResource(id = R.dimen.item_inner_padding)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomText(
                text = stringResource(id = R.string.select_all),
                style = MyAppTheme.typography.Semibold52_5,
                color = MyAppTheme.colors.gray1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Checkbox(
                checked = isSelectAll,
                onCheckedChange = { onSelectAllCategory(!isSelectAll) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MyAppTheme.colors.itemSelectedBg,
                    checkmarkColor = MyAppTheme.colors.black

                )
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
                .verticalScroll(rememberScrollState())
        ) {
            categoryList.forEach { item ->
                SelectedCategoryItem(
                    name = item.name,
                    isSelected = selectedList.contains(item.id),
                    onClick = {
                        onSelectCategory(item)
                    }
                )
            }
        }
    }
}

@Composable
fun SelectedCategoryItem(
    name: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val tintColor = getCategoryColor(name)
    val imageVector = ImageVector.vectorResource(getCategoryIcon(name))

    ListItem(
        isClickable = true,
        onClick = onClick,
        leadingIcon = {
            RoundImage(
                imageVector = imageVector,
                imageVectorSize = 20.dp,
                tint = tintColor,
                backGround = MyAppTheme.colors.brand,
                contentDescription = "category",
                modifier = Modifier.size(40.dp)
            )
        },
        content = {
            CustomText(
                text = name,
                style = MyAppTheme.typography.Semibold52_5,
                color = MyAppTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onClick() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MyAppTheme.colors.itemSelectedBg,
                    checkmarkColor = MyAppTheme.colors.black

                )
            )
        },
        isSetDivider = false,
        itemBgColor = MyAppTheme.colors.transparent,
        modifier = modifier
    )
}