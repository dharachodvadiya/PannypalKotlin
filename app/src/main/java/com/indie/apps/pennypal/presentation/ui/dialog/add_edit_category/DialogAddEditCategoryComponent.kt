package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.enum.CategoryType
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomTab
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.dialog.select_category.CategoryItem
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.getCategoryIcon
import kotlin.enums.EnumEntries

@Composable
fun AddEditCategoryDialogField(
    list: EnumEntries<CategoryType>,
    selectCategoryType: CategoryType,
    onSelect: (CategoryType) -> Unit,
    textCategory: TextFieldState,
    onCategoryNameTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = dimensionResource(id = R.dimen.padding)
        )
    ) {

        val tabItems = list.map { period ->
            TabItemInfo(
                title = when (period) {
                    CategoryType.INCOME -> R.string.received
                    CategoryType.EXPENSE -> R.string.spent
                    CategoryType.BOTH -> R.string.both
                },
                selectBgColor = MyAppTheme.colors.itemSelectedBg,
                unSelectBgColor = MyAppTheme.colors.itemBg,
                selectContentColor = MyAppTheme.colors.black,
                unSelectContentColor = MyAppTheme.colors.gray1
            )
        }

        Row(
            modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomTab(tabList = tabItems,
                selectedIndex = list.indexOf(selectCategoryType),
                onTabSelected = {
                    onSelect(list[it])
                }
            )

        }

        Spacer(Modifier.height(dimensionResource(R.dimen.padding)))

        DialogTextFieldItem(
            textState = textCategory,
            imageVector = ImageVector.vectorResource(getCategoryIcon("")),
            placeholder = R.string.add_category_placeholder,
            onTextChange = onCategoryNameTextChange
        )
        CustomText(
            text = stringResource(id = R.string.select_icon),
            style = MyAppTheme.typography.Medium46,
            color = MyAppTheme.colors.gray1
        )
        Spacer(Modifier.height(5.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            userScrollEnabled = true,) {
           items(20){
               CategoryItem(
                   name = "",
                   isSelected = false,
                   onClick = {}
               )
           }
        }

        Spacer(modifier = Modifier.fillMaxHeight(0.1f))

    }
}