package com.indie.apps.pannypal.presentation.ui.component.dialog

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.ListItem
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.RoundImage
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pannypal.presentation.ui.component.linearGradientsBrush
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme

@Composable
fun SearchDialogField(
    onAddClick: () -> Unit,
    onTextChange: (String) -> Unit,
) {
    Column {

        SearchMerchantSearchview(
            onAddClick = onAddClick,
            onTextChange = onTextChange
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        ) {
            items(15) { index ->

                SearchMerchantListItem({})
            }
        }


    }
}

@Composable
private fun SearchMerchantSearchview(
    onAddClick: () -> Unit,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
)
{
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
            onTextChange = onTextChange,
            bgColor = MyAppTheme.colors.gray0,
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            paddingValues = PaddingValues(top = 0.dp, bottom = 0.dp, start = dimensionResource(id = R.dimen.padding), end = 0.dp),
        )
        Spacer(modifier = Modifier.width(5.dp))

        PrimaryButton(
            bgColor = MyAppTheme.colors.transparent,
            borderStroke = BorderStroke(
                width = 1.dp,
                color = MyAppTheme.colors.gray2
            ),
            onClick = onAddClick,
        ){
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
                brush = linearGradientsBrush( MyAppTheme.colors.gradientBlue),
                backGround = bgColor,
                contentDescription = "person"
            )
        },
        content = {
            Column {
                Text(
                    text = "Name",
                    style = MyAppTheme.typography.Semibold56,
                    color = MyAppTheme.colors.black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Description",
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
            vertical = 5.dp)
    )
}