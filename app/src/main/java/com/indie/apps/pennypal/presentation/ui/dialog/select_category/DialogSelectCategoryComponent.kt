package com.indie.apps.pennypal.presentation.ui.dialog.select_category

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.internanal.method.getCategoryColorById
import com.indie.apps.pennypal.util.internanal.method.getCategoryIconById
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SelectCategoryDialogField(
    currentId: Long,
    categoryList: List<Category>,
    onSelectCategory: (Category) -> Unit,
    onAddCategory: () -> Unit,
    categoryId: Long = -1L,
    onAnimStop: () -> Unit,
    addCategoryAnimRun: Boolean = false

) {
    val itemAnimateScale = remember {
        Animatable(0f)
    }
    val scope = rememberCoroutineScope()
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
    ) {

        items(categoryList) { item ->

            val modifierAdd: Modifier = if (categoryId == item.id && addCategoryAnimRun) {
                scope.launch {
                    itemAnimateScale.animateTo(
                        targetValue = 1f, animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
                    )
                    if (itemAnimateScale.value == 1f) {
                        itemAnimateScale.snapTo(0f)
                        onAnimStop()
                    }
                }

                Modifier.scale(itemAnimateScale.value)
            } else {
                Modifier
            }


            CategoryItem(
                item = item,
                isSelected = currentId == item.id,
                onClick = {
                    onSelectCategory(item)
                },
                modifier = modifierAdd
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