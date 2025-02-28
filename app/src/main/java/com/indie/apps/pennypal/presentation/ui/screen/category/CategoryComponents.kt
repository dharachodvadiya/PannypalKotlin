package com.indie.apps.pennypal.presentation.ui.screen.category

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.getCategoryColorById
import com.indie.apps.pennypal.util.getCategoryIconById

@Composable
fun CategoryTopBar(
    title: String = "",
    isEditable: Boolean = false,
    isDeletable: Boolean = false,
    isSelected: Boolean = false,
    onAddClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNavigationUp: () -> Unit,
    textState: TextFieldState,
    onSearchTextChange: (String) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TopBar(
        isBackEnable = true,
        onBackClick = onNavigationUp,
        content = {
            if (!isSelected) {
                SearchView(
                    textState = textState,
                    onTextChange = onSearchTextChange,
                    trailingIcon = Icons.Default.Search,
                    bgColor = MyAppTheme.colors.lightBlue2,
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.top_bar_profile)),
                    paddingValues = PaddingValues(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = dimensionResource(id = R.dimen.padding),
                        end = 0.dp
                    )
                )
            } else {
                CustomText(
                    text = title,
                    style = MyAppTheme.typography.Semibold52_5,
                    color = MyAppTheme.colors.black
                )
            }

        },
        trailingContent = {

            if (isSelected) {
                Row {
                    if (isDeletable) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete_top),
                            contentDescription = "Delete",
                            tint = MyAppTheme.colors.black,
                            modifier = Modifier
                                .roundedCornerBackground(MyAppTheme.colors.transparent)
                                .size(25.dp)
                                .clickableWithNoRipple { onDeleteClick() }
                        )
                    }
                    if (isEditable) {
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "edit",
                            tint = MyAppTheme.colors.black,
                            modifier = Modifier
                                .roundedCornerBackground(MyAppTheme.colors.transparent)
                                .size(25.dp)
                                .clickableWithNoRipple { onEditClick() }
                        )
                    }
                }
            } else {
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

            Icon(imageVector = Icons.Default.Close,
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
            textState = TextFieldState(),
            onAddClick = {},
            onDeleteClick = {},
            onEditClick = {},
            onNavigationUp = {},
            onSearchTextChange = {}
        )
    }
}