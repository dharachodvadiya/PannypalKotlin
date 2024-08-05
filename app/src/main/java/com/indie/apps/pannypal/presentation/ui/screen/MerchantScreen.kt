package com.indie.apps.pannypal.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.DeleteAlertDialog
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantListItem
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantTopBar
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.MerchantViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MerchantScreen(
    merchantViewModel: MerchantViewModel = hiltViewModel(),
    onMerchantClick: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    isEditAddSuccess: Boolean = false,
    modifier: Modifier = Modifier
) {
    val lazyPagingData = merchantViewModel.pagedData.collectAsLazyPagingItems()
    merchantViewModel.pagingState.update(lazyPagingData)

    var isEditSuccessState by remember {
        mutableStateOf(false)
    }

    if (isEditAddSuccess != isEditSuccessState) {
        isEditSuccessState = isEditAddSuccess
        if (isEditSuccessState) merchantViewModel.setEditAddSuccess()
    }

    var openAlertDialog by remember { mutableStateOf(false) }

    var isEditable by remember { mutableStateOf(false) }
    var isDeletable by remember { mutableStateOf(false) }

    val selectedCount = merchantViewModel.selectedList.size

    if (selectedCount == 1) {
        isEditable = true
        isDeletable = true
    } else if (selectedCount > 1) {
        isEditable = false
        isDeletable = true
    } else {
        isEditable = false
        isDeletable = false
    }

    var job: Job? = null
    Scaffold(topBar = {
        MerchantTopBar(
            title = if (selectedCount > 0) "$selectedCount " + stringResource(id = R.string.selected) else "",
            textState = merchantViewModel.searchTextState,
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

        if (merchantViewModel.pagingState.isRefresh) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
            ) {
                items(count = lazyPagingData.itemCount,
                    key = lazyPagingData.itemKey { item -> item.id }
                ) { index ->
                    val data = lazyPagingData[index]
                    if (data != null) {

                        MerchantListItem(
                            item = data,
                            isSelected = merchantViewModel.selectedList.contains(data.id),
                            onClick = {
                                if (!isEditable && !isDeletable) {
                                    onMerchantClick()
                                } else {
                                    merchantViewModel.setSelectItem(data.id)
                                }

                            }, onLongClick = {
                                merchantViewModel.setSelectItem(data.id)
                            })

                        if (merchantViewModel.pagingState.isLoadMore && index == lazyPagingData.itemCount - 1) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
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
    PannyPalTheme {
        MerchantScreen(onAddClick = {}, onEditClick = {}, onMerchantClick = {})
    }
}