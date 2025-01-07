package com.indie.apps.pennypal.presentation.ui.screen.merchant

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImageWithText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.getColorFromId
import com.indie.apps.pennypal.util.getFirstCharacterUppercase

@Composable
fun MerchantTopBar(
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
        isBackEnable = isSelected,
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
fun MerchantListItem(
    item: MerchantNameAndDetails,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit,
    isSelected: Boolean = false,
    itemBgColor: Color,
    leadingIconSize: Dp = 50.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = if (isSelected) Icons.Default.Done else Icons.Default.Person
    val iconBgColor = if (isSelected) MyAppTheme.colors.brand else getColorFromId(item.id.toInt())
    val tintColor = if (isSelected) MyAppTheme.colors.black else MyAppTheme.colors.white
    //val amount = item.incomeAmount - item.expenseAmount
    //val amountColor = if (amount >= 0) MyAppTheme.colors.greenText else MyAppTheme.colors.redText

    ListItem(
        isClickable = true,
        onClick = onClick,
        onLongClick = onLongClick,
        leadingIcon = {
            /* RoundImage(
                 imageVector = imageVector,
                 imageVectorSize = 27.dp,
                 //brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                 tint = MyAppTheme.colors.black,
                 backGround = iconBgColor,
                 contentDescription = "person",
                 modifier = Modifier.size(50.dp)
             )*/

            RoundImageWithText(
                character = getFirstCharacterUppercase(item.name),
                tint = tintColor,
                backGround = iconBgColor,
                modifier = Modifier.size(leadingIconSize)
            )
        },
        content = {
            Column {
                CustomText(
                    text = item.name,
                    style = MyAppTheme.typography.Semibold52_5,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!item.details.isNullOrEmpty()) {
                    CustomText(
                        text = item.details,
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
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

        },
        isSetDivider = false,
        modifier = modifier.padding(vertical = 5.dp),
        isSelected = isSelected,
        itemBgColor = itemBgColor
    )
}

@Preview(showBackground = true)
@Composable
private fun MerchantTopBarPreview() {
    PennyPalTheme(darkTheme = true) {
        MerchantTopBar(
            textState = TextFieldState(),
            onAddClick = {},
            onDeleteClick = {},
            onEditClick = {},
            onNavigationUp = {},
            onSearchTextChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MerchantListItemPreview() {
    PennyPalTheme(darkTheme = true) {

    }
}