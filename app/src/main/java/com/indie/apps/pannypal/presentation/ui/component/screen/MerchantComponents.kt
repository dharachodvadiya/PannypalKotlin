package com.indie.apps.pannypal.presentation.ui.component.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.TopBar
import com.indie.apps.pannypal.presentation.ui.component.linearGradientsBrush
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun MerchantTopBar(
    isEditable: Boolean = false,
    isDeletable: Boolean = false,
    onAddClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNavigationUp: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TopBar(
        isBackEnable = (isEditable || isDeletable),
        onBackClick = onNavigationUp,
        content = {
            if(!isEditable && !isDeletable)
            {
                SearchView(
                    trailingIcon = Icons.Default.Search,
                    onTextChange = onSearchTextChange,
                    bgColor = MyAppTheme.colors.gray0,
                    modifier = Modifier
                        .height(dimensionResource(R.dimen.top_bar_profile)),
                    paddingValues = PaddingValues(top = 0.dp, bottom = 0.dp, start = dimensionResource(id = R.dimen.padding), end = 0.dp)
                )
            }

        },
        trailingContent = {

            if(isEditable || isDeletable)
            {
                Row {
                    if(isDeletable){
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = MyAppTheme.colors.black,
                            modifier = Modifier.clickable { onDeleteClick() }
                        )
                    }
                    if(isEditable){
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "edit",
                            tint = MyAppTheme.colors.black,
                            modifier = Modifier.clickable { onEditClick() }
                        )
                    }
                }
            }else{
                PrimaryButton(
                    bgColor = MyAppTheme.colors.white,
                    borderStroke = BorderStroke(
                        width = 1.dp,
                        color = MyAppTheme.colors.gray2
                    ),
                    onClick = onAddClick,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.top_bar_profile))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = MyAppTheme.colors.gray2
                    )
                }
            }


        },
        modifier = modifier
    )
}

@Composable
fun MerchantListItem(
    onLongClick: ()-> Unit = {},
    onClick: ()-> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    // TODO: change Round Image Icon and bg color based on amount
    val imageVector = Icons.Default.Person
    val bgColor = MyAppTheme.colors.brandBg
    val amountColor = MyAppTheme.colors.redText

    ListItem(
        isClickable = true,
        onClick = onClick,
        onLongClick = onLongClick,
        leadingIcon = {
            RoundImage(
                imageVector = imageVector,
                imageVectorSize = 27.dp,
                brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                backGround = bgColor,
                contentDescription = "person",
                modifier = Modifier.size(50.dp)
            )
        },
        content = {
            Column {
                Text(
                    text = "Name",
                    style = MyAppTheme.typography.Semibold56,
                    color = MyAppTheme.colors.black
                )
                Text(
                    text = "Description",
                    style = MyAppTheme.typography.Medium40,
                    color = MyAppTheme.colors.gray2
                )
            }
        },
        trailingContent = {
            Text(
                text = Util.getFormattedStringWithSymbole(0.0),
                style = MyAppTheme.typography.Semibold50,
                color = amountColor
            )
        },
        isSetDivider = false,
        modifier = modifier,
        paddingValues = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.padding),
            vertical = 7.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun MerchantTopBarPreview() {
    PannyPalTheme {
        MerchantTopBar(
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
    PannyPalTheme {
        MerchantListItem(
            onClick = {}
        )
    }
}