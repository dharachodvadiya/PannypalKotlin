package com.indie.apps.pennypal.presentation.ui.dialog.search_merchant

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.MyAppDialog
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun DialogSearchMerchant(
    searchMerchantViewModel: SearchMerchantViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onAddClick: () -> Unit,
    onSelectMerchant: (MerchantNameAndDetails?) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val lazyPagingData = searchMerchantViewModel.pagedData.collectAsLazyPagingItems()
    val pagingState by searchMerchantViewModel.pagingState.collectAsStateWithLifecycle()
    pagingState.update(lazyPagingData)
    val uiState by searchMerchantViewModel.searchTextState.collectAsStateWithLifecycle()

    var job: Job? = null

    /*var showAnimatedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showAnimatedDialog = true
    }*/

    //val scope = rememberCoroutineScope()
    /*AnimatedVisibility(
        visible = showAnimatedDialog,
        enter = slideInVertically { fullHeight -> (fullHeight + fullHeight / 2) } +
                expandVertically(
                    // Expand from the top.
                    expandFrom = Alignment.Top
                ) +
                fadeIn(
                    initialAlpha = 0.5f
                ),
        exit = slideOutVertically { fullHeight -> (fullHeight + fullHeight / 2) } + fadeOut()
    ) {*/
    MyAppDialog(
        isBackEnable = true,
        title = R.string.select_merchant,
        onNavigationUp = {
            /*showAnimatedDialog = false
            scope.launch {
                delay(Util.DIALOG_ANIM_DELAY)
                onNavigationUp()
            }*/
            onNavigationUp()
        },
        content = {
            SearchDialogField(
                onAddClick = {
                    /*showAnimatedDialog = false
                    scope.launch {
                        delay(Util.DIALOG_ANIM_DELAY)
                        onAddClick()
                    }*/
                    onAddClick()
                },
                onItemClick = { data ->
                    /*showAnimatedDialog = false
                    scope.launch {
                        delay(Util.DIALOG_ANIM_DELAY)
                        onSelectMerchant(data)
                    }*/
                    onSelectMerchant(data)
                },
                textState = uiState,
                onTextChange = {
                    job?.cancel()
                    job = MainScope().launch {
                        delay(Util.SEARCH_NEWS_TIME_DELAY)
                        searchMerchantViewModel.searchData()
                    }
                },
                dataList = lazyPagingData,
                isRefresh = pagingState.isRefresh,
                isLoadMore = pagingState.isLoadMore
            )
        },
        modifier = modifier,
        isFixHeight = true
    )
    // }
}

@Preview
@Composable
private fun MyAppDialogPreview() {
    PennyPalTheme(darkTheme = true) {
        DialogSearchMerchant(
            onNavigationUp = {},
            onAddClick = {},
            onSelectMerchant = {}
        )
    }
}