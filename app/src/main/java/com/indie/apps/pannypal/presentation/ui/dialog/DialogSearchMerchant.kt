package com.indie.apps.pannypal.presentation.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.component.dialog.MerchantNameAndDetailsList
import com.indie.apps.pannypal.presentation.ui.component.dialog.SearchDialogField
import com.indie.apps.pannypal.presentation.ui.state.TextFieldState
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.SearchMerchantViewModel
import com.indie.apps.pannypal.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DialogSearchMerchant(
    searchMerchantViewModel: SearchMerchantViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by searchMerchantViewModel.uiState.collectAsStateWithLifecycle()

    val dataList = remember { mutableStateOf(MerchantNameAndDetailsList()) }

    val isLoading = remember {
        mutableStateOf(false)
    }

    when(uiState){
        is Resource.Loading -> isLoading.value = true
        is Resource.Success ->{
            dataList.value = MerchantNameAndDetailsList(uiState.data)
            isLoading.value = false
        }
        is Resource.Error -> {}
    }
    var job: Job? = null
    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_merchant,
        onNavigationUp = onNavigationUp,
        content = {
            SearchDialogField(
                onAddClick = onAddClick,
                onItemClick = {searchMerchantViewModel.loadMoreData()},
                textState = searchMerchantViewModel.searchTextState,
                //onTextChange = {searchMerchantViewModel.searchData(it)},
                onTextChange = {
                    job?.cancel()
                    job = MainScope().launch {
                        delay(Util.SEARCH_NEWS_TIME_DELAY)
                        searchMerchantViewModel.searchData(it)
                    }
                },
                data = dataList.value,
                isLoading = isLoading.value
            )
        },
        modifier = modifier,
        isFixHeight = true
    )
}

@Preview()
@Composable
private fun MyAppDialogPreview() {
    PannyPalTheme {
        DialogSearchMerchant(
            onNavigationUp = {},
            onAddClick = {}
        )
    }
}