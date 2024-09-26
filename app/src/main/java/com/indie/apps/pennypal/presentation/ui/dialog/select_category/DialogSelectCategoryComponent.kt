package com.indie.apps.pennypal.presentation.ui.dialog.select_category

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.getCategoryColor
import com.indie.apps.pennypal.util.getCategoryIcon

@Composable
fun SelectCategoryDialogField(
    currentId: Long,
    categoryList: List<Category>,
    onSelectCategory: (Category) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
    ) {
        items(categoryList) { item ->
            CategoryItem(
                name = item.name,
                isSelected = currentId == item.id,
                onClick = {
                    onSelectCategory(item)
                }
            )
        }
    }
}

@Composable
fun CategoryItem(
    name: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val tintColor = getCategoryColor(name)
    val bgColor = if (isSelected) MyAppTheme.colors.itemSelectedBg else MyAppTheme.colors.brand
    val imageColor = if (isSelected) MyAppTheme.colors.black else tintColor
    val imageVector = ImageVector.vectorResource(getCategoryIcon(name))


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .roundedCornerBackground(backgroundColor = MyAppTheme.colors.transparent)
            .clickable {
                onClick()
            }
    ) {
        RoundImage(
            imageVector = imageVector,
            tint = imageColor,
            backGround = bgColor,
            contentDescription = "category",
            innerPadding = dimensionResource(id = R.dimen.item_inner_padding)
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.item_padding)))

        CustomText(
            text = name,
            style = MyAppTheme.typography.Medium40,
            color = MyAppTheme.colors.gray1,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}