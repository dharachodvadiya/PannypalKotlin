package com.indie.apps.pennypal.presentation.ui.dialog.search_merchant

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.DialogSearchView
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.CustomText
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImageWithText
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.getColorFromId
import com.indie.apps.pennypal.util.getFirstCharacterUppercase

/*

@Immutable
data class MerchantNameAndDetailsList(val dataList: List<MerchantNameAndDetails>? = null)
*/

@Composable
fun SearchDialogField(
    onAddClick: () -> Unit,
    onItemClick: (MerchantNameAndDetails?) -> Unit,
    onTextChange: (String) -> Unit,
    textState: TextFieldState,
    dataList: LazyPagingItems<MerchantNameAndDetails>,
    isRefresh: Boolean = false,
    isLoadMore: Boolean = false
) {
    Column {

        /* SearchMerchantSearchView(
             onAddClick = onAddClick,
             textState = textState,
             onTextChange = onTextChange
         )*/

        DialogSearchView(
            searchState = textState,
            onTextChange = onTextChange,
            trailingContent = {
                PrimaryButton(
                    bgColor = MyAppTheme.colors.white,
                    borderStroke = BorderStroke(
                        width = 1.dp,
                        color = MyAppTheme.colors.gray2
                    ),
                    onClick = onAddClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = MyAppTheme.colors.gray2
                    )
                }
            }
        )



        if (isRefresh) {
            LoadingWithProgress(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else if (dataList.itemCount == 0) {
            NoDataMessage(
                title = stringResource(id = R.string.no_merchants),
                details = stringResource(id = R.string.no_merchants_details),
                iconSize = 50.dp,
                painterRes = R.drawable.person_off,
                titleTextStyle = MyAppTheme.typography.Regular46,
                detailsTextStyle = MyAppTheme.typography.Regular44
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.padding)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_padding))
            ) {
                items(
                    count = dataList.itemCount,
                    key = dataList.itemKey { item -> item.id },
                    contentType = dataList.itemContentType { "MerchantNameAndDetails" }
                ) { index ->
                    val data = dataList[index]
                    if (data != null) {
                        SearchMerchantListItem(
                            item = data,
                            onClick = { onItemClick(data) }
                        )

                        /* MerchantListItem(
                             item = data,
                             isSelected = false,
                             onClick = {
                                 onItemClick(data)
                             },
                             onLongClick = { },
                             leadingIconSize = 40.dp,
                             itemBgColor = MyAppTheme.colors.transparent
                         )*/

                        if (isLoadMore && index == dataList.itemCount - 1) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                CircularProgressIndicator()
                            }

                        }
                    }
                }
            }
        }

    }
}

/*@Composable
private fun SearchMerchantSearchView(
    onAddClick: () -> Unit,
    onTextChange: (String) -> Unit,
    textState: TextFieldState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding),
                vertical = 7.dp
            )
    ) {
        SearchView(
            trailingIcon = Icons.Default.Search,
            bgColor = MyAppTheme.colors.lightBlue2,
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            paddingValues = PaddingValues(
                top = 0.dp,
                bottom = 0.dp,
                start = dimensionResource(id = R.dimen.padding),
                end = 0.dp
            ),
            textState = textState,
            onTextChange = onTextChange
        )
        Spacer(modifier = Modifier.width(5.dp))

        PrimaryButton(
            bgColor = MyAppTheme.colors.transparent,
            borderStroke = BorderStroke(
                width = 1.dp,
                color = MyAppTheme.colors.gray2
            ),
            onClick = onAddClick,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = MyAppTheme.colors.gray2
            )
        }

    }
}*/

@Composable
private fun SearchMerchantListItem(
    item: MerchantNameAndDetails,
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val iconBgColor = getColorFromId(item.id.toInt())
    val tintColor = MyAppTheme.colors.white

    ListItem(
        onClick = onClick,
        leadingIcon = {
            RoundImageWithText(
                character = getFirstCharacterUppercase(item.name),
                tint = tintColor,
                backGround = iconBgColor,
                modifier = Modifier.size(40.dp)
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
                        style = MyAppTheme.typography.Medium33,
                        color = MyAppTheme.colors.gray1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        },
        modifier = modifier,
        paddingValues = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.padding)
        ),
        itemBgColor = MyAppTheme.colors.transparent
    )
}
