package com.indie.apps.pennypal.presentation.ui.dialog.select_category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
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
    onSelectCategory: (Category) -> Unit,
    onAddCategory: () -> Unit,
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
        item {
            CategoryItem(
                name = "",
                isSelected = false,
                imageVector = Icons.Default.Add,
                imageColor = MyAppTheme.colors.gray1,
                bgColor = MyAppTheme.colors.gray3.copy(alpha = 0.5f),
                onClick = onAddCategory
            )
        }
    }
}

@Composable
fun CategoryItem(
    name: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    imageColor: Color? = null,
    bgColor: Color? = null,
    imageVector: ImageVector? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val tintColor = getCategoryColor(name)
    val bgColor1 = if (isSelected) MyAppTheme.colors.itemSelectedBg else MyAppTheme.colors.brand
    val imageColor1 = if (isSelected) MyAppTheme.colors.black else tintColor
    val imageVector1 = ImageVector.vectorResource(getCategoryIcon(name))


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .roundedCornerBackground(backgroundColor = MyAppTheme.colors.transparent)
            .clickableWithNoRipple {
                onClick()
            }
    ) {
        RoundImage(
            imageVector = imageVector ?: imageVector1,
            tint = imageColor ?: imageColor1,
            backGround = bgColor ?: bgColor1,
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