package com.indie.apps.pannypal.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.DeleteAlertDialog
import com.indie.apps.pannypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantListItem
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantTopBar
import com.indie.apps.pannypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.MerchantViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MerchantScreen(
    merchantViewModel: MerchantViewModel = hiltViewModel(),
    onMerchantClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    isEditAddSuccess: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val merchantDeleteToast = stringResource(id = R.string.merchant_delete_success_message)
    val lazyPagingData = merchantViewModel.pagedData.collectAsLazyPagingItems()
    val pagingState by merchantViewModel.pagingState.collectAsStateWithLifecycle()
    pagingState.update(lazyPagingData)

    val searchTextState by merchantViewModel.searchTextState.collectAsStateWithLifecycle()
    val isEditable by merchantViewModel.isEditable.collectAsStateWithLifecycle()
    val isDeletable by merchantViewModel.isDeletable.collectAsStateWithLifecycle()
    val scrollOffset by merchantViewModel.scrollOffset.collectAsStateWithLifecycle()
    val scrollIndex by merchantViewModel.scrollIndex.collectAsStateWithLifecycle()
    val selectedList = merchantViewModel.selectedList

    var isEditSuccessState by remember {
        mutableStateOf(false)
    }

    if (isEditAddSuccess != isEditSuccessState) {
        isEditSuccessState = isEditAddSuccess
        if (isEditSuccessState) {
            merchantViewModel.setEditAddSuccess()
        }
    }

    var openAlertDialog by remember { mutableStateOf(false) }

    var job: Job? = null
    Scaffold(topBar = {
        MerchantTopBar(
            title = if (selectedList.size > 0) "${selectedList.size} " + stringResource(
                id = R.string.selected_text
            ) else "",
            textState = searchTextState,
            isEditable = isEditable,
            isDeletable = isDeletable,
            onAddClick = { merchantViewModel.onAddClick { onAddClick() } },
            onEditClick = { merchantViewModel.onEditClick { onEditClick(it) } },
            onDeleteClick = { merchantViewModel.onDeleteClick { openAlertDialog = true } },
            onNavigationUp = { merchantViewModel.onNavigationUp { } },
            onSearchTextChange = {
                job?.cancel()
                job = MainScope().launch {
                    delay(Util.SEARCH_NEWS_TIME_DELAY)
                    merchantViewModel.searchData()
                }
            })
    }) { innerPadding ->

        if (pagingState.isRefresh) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {

            val scrollState: LazyListState = rememberLazyListState(
                scrollIndex,
                scrollOffset
            )

            // after each scroll, update values in ViewModel
            LaunchedEffect(key1 = scrollState.isScrollInProgress) {
                if (!scrollState.isScrollInProgress) {
                    merchantViewModel.setScrollVal(
                        scrollIndex = scrollState.firstVisibleItemIndex,
                        scrollOffset = scrollState.firstVisibleItemScrollOffset
                    )
                }
            }

            LazyColumn(
                state = scrollState,
                modifier = modifier
                    .fillMaxSize()
                    .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                    .padding(innerPadding)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_inner_padding))
            ) {
                items(count = lazyPagingData.itemCount,
                    key = lazyPagingData.itemKey { item -> item.id }
                ) { index ->
                    val data = lazyPagingData[index]
                    if (data != null) {

                        MerchantListItem(
                            item = data,
                            isSelected = selectedList.contains(data.id),
                            onClick = { merchantViewModel.onItemClick(data.id) { onMerchantClick(it) } },
                            onLongClick = { merchantViewModel.onItemLongClick(data.id) }
                        )

                        if (pagingState.isLoadMore && index == lazyPagingData.itemCount - 1) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MyAppTheme.colors.transparent)
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }


        if (openAlertDialog) {
            DeleteAlertDialog(
                dialogTitle = R.string.delete_dialog_title,
                dialogText = R.string.delete_item_dialog_text,
                onConfirmation = {
                    merchantViewModel.onDeleteDialogClick {
                        openAlertDialog = false
                        Toast.makeText(context, merchantDeleteToast, Toast.LENGTH_SHORT).show()
                    }
                },
                onDismissRequest = { openAlertDialog = false }
            )
        }


    }
}

@Preview
@Composable
private fun MerchantScreenPreview() {
    PannyPalTheme(darkTheme = true) {
        MerchantScreen(onAddClick = {}, onEditClick = {}, onMerchantClick = {})
    }
}