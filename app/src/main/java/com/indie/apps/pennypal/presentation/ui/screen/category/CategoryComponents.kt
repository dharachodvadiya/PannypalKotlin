package com.indie.apps.pennypal.presentation.ui.screen.category

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.db_entity.Category
import com.indie.apps.pennypal.presentation.ui.component.composable.common.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.ListItem
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.internanal.method.getCategoryColorById
import com.indie.apps.pennypal.util.internanal.method.getCategoryIconById

@Composable
fun CategoryTopBar(
    title: String = "",
    onAddClick: () -> Unit,
    onNavigationUp: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TopBarWithTitle(
        title = title,
        isBackEnable = true,
        contentAlignment = Alignment.Center,
        onNavigationUp = onNavigationUp,
        trailingContent = {
            PrimaryButton(
                bgColor = MyAppTheme.colors.white,
                borderStroke = BorderStroke(
                    width = 1.dp,
                    color = MyAppTheme.colors.gray1
                ),
                onClick = onAddClick,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.top_bar_profile))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MyAppTheme.colors.gray1
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun CategoryListItem(
    item: Category,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isSelected: Boolean = false,
    itemBgColor: Color,
    leadingIconSize: Dp = 40.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val tintColor = getCategoryColorById(item.iconColorId)
    val bgColor1 = MyAppTheme.colors.brand
    val imageColor1 = if (isSelected) MyAppTheme.colors.black else tintColor
    //val amount = item.incomeAmount - item.expenseAmount
    //val amountColor = if (amount >= 0) MyAppTheme.colors.greenText else MyAppTheme.colors.redText

    ListItem(
        isClickable = true,
        onClick = onClick,
        onLongClick = onLongClick,
        leadingIcon = {

            RoundImage(
                imageVector = ImageVector.vectorResource(
                    getCategoryIconById(
                        item.iconId,
                        LocalContext.current
                    )
                ),
                imageVectorSize = 20.dp,
                tint = imageColor1,
                backGround = bgColor1,
                modifier = Modifier.size(leadingIconSize),
                contentDescription = item.name
            )
        },
        content = {
            CustomText(
                text = item.name,
                style = MyAppTheme.typography.Semibold52_5,
                color = MyAppTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            /* CustomText(
                 text = Util.getFormattedStringWithSymbol(amount),
                 style = MyAppTheme.typography.Regular51,
                 color = amountColor,
                 overflow = TextOverflow.Ellipsis,
                 modifier = Modifier.fillMaxWidth(0.5f),
                 textAlign = TextAlign.Right,
                 maxLines = 1
             )*/

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "edit",
                modifier = Modifier
                    .roundedCornerBackground(MyAppTheme.colors.transparent)
                    .clickableWithNoRipple { onDeleteClick() })

        },
        isSetDivider = false,
        modifier = modifier.padding(vertical = 5.dp),
        isSelected = isSelected,
        itemBgColor = itemBgColor
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryTopBarPreview() {
    PennyPalTheme(darkTheme = true) {
        CategoryTopBar(
            onAddClick = {},
            onNavigationUp = {},
        )
    }
}