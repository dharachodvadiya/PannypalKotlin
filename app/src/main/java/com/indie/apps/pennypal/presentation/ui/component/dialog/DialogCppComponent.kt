package com.indie.apps.pennypal.presentation.ui.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.indie.apps.cpp.data.model.Country
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.viewmodel.CppViewModel

@Composable
fun CppDialogField(
    viewModel: CppViewModel,
    onSelect: (Country) -> Unit,
    searchState: TextFieldState,
    countriesList: List<Country>,
    onTextChange: (String) -> Unit,
    isShowCurrency: Boolean,
) {
    Column {

        SearchCcpSearchView(
            searchState = searchState,
            onTextChange = onTextChange
        )
        //val context = LocalContext.current
        /*val countries = remember { countryList(context) }
        val countriesList = countries.toList()*/

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        ) {
            if (isShowCurrency) {
                items(
                    countriesList
                ) { country ->
                    SearchCurrencyCppListItem(
                        country = country,
                        onClick = onSelect,
                        flagId = viewModel.getFlagIdFromCountryCode(country.countryCode)
                    )
                }
            } else {
                items(
                    countriesList
                ) { country ->
                    SearchCppListItem(
                        country = country,
                        onClick = onSelect,
                        flagId = viewModel.getFlagIdFromCountryCode(country.countryCode)
                    )
                }
            }

        }

    }
}

@Composable
private fun SearchCcpSearchView(
    searchState: TextFieldState,
    onTextChange: (String) -> Unit,
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
            onTextChange = onTextChange
        )
    }
}

@Composable
private fun SearchCppListItem(
    country: Country,
    flagId: Int,
    onClick: (Country) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Row(
        modifier = modifier
            .clickable {
                onClick(country)
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(flagId),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(20.dp)
                .width(30.dp)
        )
        Text(
            text = country.name,
            modifier = Modifier
                .padding(start = 8.dp),
            style = MyAppTheme.typography.Medium45_29
        )
        Text(
            text = "(${country.dialCode})",
            modifier = Modifier
                .padding(start = 8.dp),
            style = MyAppTheme.typography.Medium50,
            color = MyAppTheme.colors.gray2
        )

    }
}

@Composable
private fun SearchCurrencyCppListItem(
    flagId: Int,
    country: Country,
    onClick: (Country) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Row(
        modifier = modifier
            .clickable {
                onClick(country)
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(flagId),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(20.dp)
                .width(30.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(
                text = "${country.currencyCode} (${country.currencySymbol})",
                modifier = Modifier,
                style = MyAppTheme.typography.Medium45_29,
                color = MyAppTheme.colors.black
            )
            Text(
                text = country.currencyName,
                modifier = Modifier,
                style = MyAppTheme.typography.Medium33,
                color = MyAppTheme.colors.gray2
            )
        }

    }
}