package com.indie.apps.pennypal.presentation.ui.dialog.select_category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.db_entity.Category
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.addAnim
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.app_enum.AnimationType
import com.indie.apps.pennypal.util.internanal.method.getCategoryColorById
import com.indie.apps.pennypal.util.internanal.method.getCategoryIconById

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SelectCategoryDialogField(
    currentId: Long,
    categoryList: List<Category>,
    onSelectCategory: (Category) -> Unit,
    onAddCategory: () -> Unit,
    categoryAnimId: Long = -1L,
    onAnimationComplete: (AnimationType) -> Unit,
    currentAnim: AnimationType = AnimationType.NONE
) {
    val scope = rememberCoroutineScope()
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
    ) {

        items(categoryList) { item ->

            val modifierAnim = if (categoryAnimId == item.id) {
                when (currentAnim) {
                    AnimationType.ADD -> Modifier.addAnim(scope) {
                        onAnimationComplete(
                            AnimationType.ADD
                        )
                    }

                    else -> Modifier
                }
            } else Modifier


            CategoryItem(
                item = item,
                isSelected = currentId == item.id,
                onClick = {
                    onSelectCategory(item)
                },
                modifier = modifierAnim
            )
        }
        item {
            CategoryItem(
                item = Category(name = ""),
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
    item: Category,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    imageColor: Color? = null,
    bgColor: Color? = null,
    imageVector: ImageVector? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val tintColor = getCategoryColorById(item.iconColorId)
    val bgColor1 = if (isSelected) MyAppTheme.colors.itemSelectedBg else MyAppTheme.colors.brand
    val imageColor1 = if (isSelected) MyAppTheme.colors.black else tintColor
    val imageVector1 =
        ImageVector.vectorResource(getCategoryIconById(item.iconId, LocalContext.current))


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
            text = item.name,
            style = MyAppTheme.typography.Medium40,
            color = MyAppTheme.colors.gray1,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun CategoryColorItem(
    colorId: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val tintColor = getCategoryColorById(colorId)
    val bgColor1 = if (isSelected) tintColor else MyAppTheme.colors.brand
    val imageVector1 = Icons.Filled.Circle

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .roundedCornerBackground(backgroundColor = MyAppTheme.colors.transparent)
            .clickableWithNoRipple {
                onClick()
            }
    ) {
        RoundImage(
            imageVector = imageVector1,
            tint = tintColor,
            backGround = bgColor1,
            contentDescription = "category",
            innerPadding = dimensionResource(id = R.dimen.item_inner_padding)
        )
    }
}