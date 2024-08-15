package com.indie.apps.pennypal.presentation.ui.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.indie.apps.cpp.data.utils.getFlags
import com.indie.apps.cpp.utils.Country
import com.indie.apps.cpp.utils.countryList
import com.indie.apps.cpp.utils.searchCountry
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import java.util.Locale

@Composable
fun CppDialogField(
    onSelect: (Country) -> Unit,
    searchState: TextFieldState,
) {
    Column {

        SearchCcpSearchview(
            searchState = searchState
        )
        val context = LocalContext.current
        val countries = remember { countryList(context) }
        val countriesList = countries.toList()

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        ) {
            items(
                if (searchState.text.isEmpty()) {
                    countriesList
                } else {
                    countriesList.searchCountry(searchState.text)
                }
            ) { country ->
                SearchCppListItem(
                    country = country,
                    onClick = onSelect
                )
            }
        }

    }
}

@Composable
private fun SearchCcpSearchview(
    searchState: TextFieldState,
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
            textState = searchState,
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
            onTextChange = {}
        )
    }
}

@Composable
private fun SearchCppListItem(
    country: Country,
    onClick: (Country) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val code = country.code.lowercase(Locale.ROOT)
    Row(modifier = modifier
        .clickable {
            onClick(country)
        }
        .padding(12.dp)) {
//                            Text(text = ("${localeToEmoji(mlist.code)}"))
//                            Text(text = ("${getFlags(it.code.toLowerCase())}"))
        Image(
            painterResource(getFlags(code)),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(20.dp)
                .width(20.dp)
        )
        Text(
            text = country.name,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(2f),
            style = MyAppTheme.typography.Regular44
        )
    }
    /*Divider(
        color = Color.LightGray, thickness = 0.5.dp
    )*/
}