package com.indie.apps.pannypal.presentation.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pannypal.presentation.ui.component.dialog.SearchDialogField
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.SearchMerchantViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DialogSearchMerchant(
    searchMerchantViewModel: SearchMerchantViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onAddClick: () -> Unit,
    onSelectMerchant: (MerchantNameAndDetails?) -> Unit,
    modifier: Modifier = Modifier
) {

    val lazyPagingData = searchMerchantViewModel.pagedData.collectAsLazyPagingItems()
    searchMerchantViewModel.pagingState.update(lazyPagingData)

    //val uiState by searchMerchantViewModel.uiState.collectAsStateWithLifecycle()

    var job: Job? = null
    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_merchant,
        onNavigationUp = onNavigationUp,
        content = {
            SearchDialogField(
                onAddClick = onAddClick,
                onItemClick = onSelectMerchant,
                textState = searchMerchantViewModel.searchTextState,
                onTextChange = {
                    job?.cancel()
                    job = MainScope().launch {
                        delay(Util.SEARCH_NEWS_TIME_DELAY)
                        searchMerchantViewModel.searchData()
                    }
                },
                dataList = lazyPagingData,
                isRefresh = searchMerchantViewModel.pagingState.isRefresh,
                isLoadMore = searchMerchantViewModel.pagingState.isLoadMore
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
            onAddClick = {},
            onSelectMerchant = {}
        )
    }
}