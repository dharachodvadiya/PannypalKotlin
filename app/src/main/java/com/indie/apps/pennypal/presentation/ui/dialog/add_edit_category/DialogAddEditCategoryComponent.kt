package com.indie.apps.pennypal.presentation.ui.dialog.add_edit_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.enum.CategoryType
import com.indie.apps.pennypal.data.module.TabItemInfo
import com.indie.apps.pennypal.presentation.ui.component.DialogTextFieldItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomTab
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.dialog.select_category.CategoryColorItem
import com.indie.apps.pennypal.presentation.ui.dialog.select_category.CategoryItem
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.getCategoryColorById
import com.indie.apps.pennypal.util.getCategoryIconById
import kotlin.enums.EnumEntries

@Composable
fun AddEditCategoryDialogField(
    list: EnumEntries<CategoryType>,
    selectCategoryType: CategoryType,
    onSelectCategoryType: (CategoryType) -> Unit,
    selectedIcon: Int,
    onSelectCategoryIcon: (Int) -> Unit,
    selectedIconColor: Int,
    onSelectCategoryIconColor: (Int) -> Unit,
    textCategory: TextFieldState,
    onCategoryNameTextChange: (String) -> Unit,
    focusRequesterName: FocusRequester,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding)
            )
            .verticalScroll(rememberScrollState())
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
            CustomTab(
                tabList = tabItems,
                selectedIndex = list.indexOf(selectCategoryType),
                onTabSelected = {
                    onSelectCategoryType(list[it])
                }
            )

        }

        Spacer(Modifier.height(dimensionResource(R.dimen.padding)))

        DialogTextFieldItem(
            textState = textCategory,
            leadingIcon = {
                val icon = ImageVector.vectorResource(
                    getCategoryIconById(
                        selectedIcon,
                        LocalContext.current
                    )
                )
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = getCategoryColorById(selectedIconColor),
                )
            },
            placeholder = R.string.add_category_placeholder,
            focusRequester = focusRequesterName,
            onTextChange = onCategoryNameTextChange
        )


        CustomText(
            text = stringResource(id = R.string.select_icon_color),
            style = MyAppTheme.typography.Medium46,
            color = MyAppTheme.colors.gray1
        )
        Spacer(Modifier.height(5.dp))


        LazyRow(
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding)),
            userScrollEnabled = true
        ) {
            items(15) { index ->
                val id = index + 1
                CategoryColorItem(
                    colorId = id,
                    isSelected = selectedIconColor == id,
                    onClick = {
                        onSelectCategoryIconColor(id)
                    }
                )
            }
        }

        CustomText(
            text = stringResource(id = R.string.select_icon),
            style = MyAppTheme.typography.Medium46,
            color = MyAppTheme.colors.gray1
        )
        Spacer(Modifier.height(5.dp))

        LazyRow(
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding)),
            userScrollEnabled = true
        ) {
            items(15) { index ->
                val id = index + 1
                CategoryItem(
                    item = Category(name = "", iconColorId = 1, iconId = id),
                    isSelected = selectedIcon == id,
                    onClick = {
                        onSelectCategoryIcon(id)
                    }
                )
            }
        }




        Spacer(modifier = Modifier.fillMaxHeight(0.1f))

    }
}