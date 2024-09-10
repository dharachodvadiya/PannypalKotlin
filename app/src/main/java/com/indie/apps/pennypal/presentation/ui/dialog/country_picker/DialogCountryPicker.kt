package com.indie.apps.pennypal.presentation.ui.dialog.country_picker

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.cpp.data.model.Country
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@Composable
fun DialogCountryPicker(
    viewModel: CountryPickerViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onSelect: (Country) -> Unit,
    onSaveSuccess: () -> Unit,
    selectedCountryCode: String,
    isSavable: Boolean = false,
    isShowCurrency: Boolean,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val searchTextState by viewModel.searchTextState.collectAsStateWithLifecycle()
    val countryData by viewModel.uiState.collectAsStateWithLifecycle()

    var currentCountryCode by remember {
        mutableStateOf(selectedCountryCode)
    }

    var job: Job? = null

    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_country,
        onNavigationUp = onNavigationUp,
        content = {
            CppDialogField(
                viewModel= viewModel,
                onSelect = {
                    if(!isSavable) {
                        onSelect(it)
                    }else{
                        currentCountryCode = it.countryCode
                    }
                },
                searchState = searchTextState,
                isShowCurrency = isShowCurrency,
                countriesList = countryData,
                isSelectable = isSavable,
                currentCountry = currentCountryCode,
                onTextChange = {
                    job?.cancel()
                    job = MainScope().launch {
                        delay(Util.SEARCH_NEWS_TIME_DELAY)
                        viewModel.searchData(isShowCurrency)
                    }
                }
            )
        },
        bottomContent = {
            if(isSavable){
                BottomSaveButton(
                    onClick = {
                        viewModel.saveDefaultCurrency(currentCountryCode){
                            onSaveSuccess()
                        }
                    },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding))
                )
            }
        },
        modifier = modifier,
        isFixHeight = true
    )
}

@Preview
@Composable
private fun DialogCppPreview() {
    PennyPalTheme(darkTheme = true) {
        //DialogCpp({},{})
    }
}