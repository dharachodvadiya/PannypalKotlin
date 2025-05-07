package com.indie.apps.pennypal.presentation.ui.dialog.country_picker

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.indie.apps.cpp.data.model.Country
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.common.DialogSearchView
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.state.TextFieldState
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme

@Composable
fun CppDialogField(
    viewModel: CountryPickerViewModel,
    isSelectable: Boolean,
    currentCountry: String,
    onSelect: (Country) -> Unit,
    searchState: TextFieldState,
    countriesList: List<Country>,
    onTextChange: (String) -> Unit,
    isShowCurrency: Boolean,
    focusRequesterSearch: FocusRequester,
) {
    Column {

        DialogSearchView(
            searchState = searchState,
            onTextChange = onTextChange,
            focusRequesterSearch = focusRequesterSearch
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
                        isSelectable = isSelectable,
                        currentCountryCode = currentCountry,
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
private fun SearchCppListItem(
    country: Country,
    flagId: Int,
    onClick: (Country) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    Row(
        modifier = modifier
            .roundedCornerBackground(MyAppTheme.colors.transparent)
            .clickableWithNoRipple {
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
        CustomText(
            text = country.name,
            modifier = Modifier
                .padding(start = 8.dp),
            style = MyAppTheme.typography.Medium45_29
        )
        CustomText(
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
    isSelectable: Boolean,
    currentCountryCode: String,
    flagId: Int,
    country: Country,
    onClick: (Country) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
) {
    Row(
        modifier = modifier
            .height(55.dp)
            .roundedCornerBackground(MyAppTheme.colors.transparent)
            .clickableWithNoRipple {
                onClick(country)
            }
            .padding(horizontal = dimensionResource(id = R.dimen.item_inner_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelectable) {
            RadioButton(
                selected = currentCountryCode.lowercase() == country.countryCode.lowercase(),
                onClick = {
                    onClick(country)
                })

        }
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
            CustomText(
                text = "${country.currencyCode} (${country.currencySymbol})",
                modifier = Modifier,
                style = MyAppTheme.typography.Medium45_29,
                color = MyAppTheme.colors.black
            )
            CustomText(
                text = country.currencyName,
                modifier = Modifier,
                style = MyAppTheme.typography.Medium33,
                color = MyAppTheme.colors.gray2
            )
        }

    }
}