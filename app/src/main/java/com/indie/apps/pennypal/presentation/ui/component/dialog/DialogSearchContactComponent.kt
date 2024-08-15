package com.indie.apps.pennypal.presentation.ui.component.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

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

        SearchMerchantSearchView(
            onAddClick = onAddClick,
            textState = textState,
            onTextChange = onTextChange
        )
        if (isRefresh) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)

            ) {
                CircularProgressIndicator()
            }
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

@Composable
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
}

@Composable
private fun SearchMerchantListItem(
    item: MerchantNameAndDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val imageVector = Icons.Default.Person
    val bgColor = MyAppTheme.colors.lightBlue2


    ListItem(
        onClick = onClick,
        leadingIcon = {
            RoundImage(
                imageVector = imageVector,
                //brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                tint = MyAppTheme.colors.black,
                backGround = bgColor,
                contentDescription = "person"
            )
        },
        content = {
            Column {
                Text(
                    text = item.name,
                    style = MyAppTheme.typography.Semibold52_5,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (item.details.isNullOrEmpty()) stringResource(id = R.string.no_details) else item.details,
                    style = MyAppTheme.typography.Medium33,
                    color = MyAppTheme.colors.gray1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        modifier = modifier,
        paddingValues = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.padding)
        ),
        itemBgColor = MyAppTheme.colors.transparent
    )
}