package com.indie.apps.pannypal.presentation.ui.component.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pannypal.presentation.ui.component.linearGradientsBrush
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme

@Immutable
data class MerchantNameAndDetailsList(val dataList: List<MerchantNameAndDetails>? = null)

@Composable
fun SearchDialogField(
    onAddClick: () -> Unit,
    onItemClick: (MerchantNameAndDetails) -> Unit,
    onTextChange: (String) -> Unit,
    textState: TextFieldState,
    data: MerchantNameAndDetailsList,
    isLoading : Boolean
    ) {
    Column {

        SearchMerchantSearchView(
            onAddClick = onAddClick,
            textState = textState,
            onTextChange = onTextChange
        )
        if (!data.dataList.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.padding))
            ) {
                val dataSize = data.dataList.size
                itemsIndexed(
                    items = data.dataList,
                    key = { index, item -> item.id }
                ) { index, item ->
                    SearchMerchantListItem(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                    if(index == dataSize-1 && isLoading){
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(dimensionResource(id = R.dimen.loading_smalll_size)))
                        }

                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                if(isLoading){
                    CircularProgressIndicator()
                }else{
                    Text(
                        text = stringResource(id = R.string.no_data_found),
                        style = MyAppTheme.typography.Medium40,
                        color = MyAppTheme.colors.gray2
                    )
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
            bgColor = MyAppTheme.colors.gray0,
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
    val bgColor = MyAppTheme.colors.brandBg


    ListItem(
        onClick = onClick,
        leadingIcon = {
            RoundImage(
                imageVector = imageVector,
                brush = linearGradientsBrush(MyAppTheme.colors.gradientBlue),
                backGround = bgColor,
                contentDescription = "person"
            )
        },
        content = {
            Column {
                Text(
                    text = item.name,
                    style = MyAppTheme.typography.Semibold56,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if(item.details.isNullOrEmpty()) stringResource(id = R.string.no_details) else item.details,
                    style = MyAppTheme.typography.Medium33,
                    color = MyAppTheme.colors.gray2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        modifier = modifier,
        paddingValues = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.padding),
            vertical = 5.dp
        )
    )
}