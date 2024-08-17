package com.indie.apps.pennypal.presentation.ui.screen

import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.DeleteAlertDialog
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.screen.MerchantListItem
import com.indie.apps.pennypal.presentation.ui.component.screen.MerchantTopBar
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.presentation.viewmodel.MerchantViewModel
import com.indie.apps.pennypal.util.Util
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
    isEditSuccess: Boolean = false,
    isAddSuccess: Boolean = false,
    isAddMerchantDataSuccess: Boolean = false,
    editAddId: Long = 1L,
    merchantId: Long = 1L,
    bottomPadding: PaddingValues,
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

    /* var isEditSuccessState by remember {
         mutableStateOf(false)
     }*/

    /*var addItemId by remember {
        mutableStateOf(-1L)
    }

    var editItemId by remember {
        mutableStateOf(-1L)
    }*/

    /*if (isEditAddSuccess != isEditSuccessState) {
        isEditSuccessState = isEditAddSuccess
        if (isEditSuccessState) {
            addOrEditItemId = editAddId
            merchantViewModel.setEditAddSuccess()
        }
    }*/
    LaunchedEffect(isAddSuccess) {
        if (isAddSuccess) {
            merchantViewModel.addSuccess()
            //addItemId = editAddId
        }

    }

    LaunchedEffect(isEditSuccess) {
        if (isEditSuccess) {
            merchantViewModel.editSuccess()
            //editItemId = editAddId
        }

    }

    LaunchedEffect(isAddMerchantDataSuccess) {
        if (isAddMerchantDataSuccess) {
            merchantViewModel.addMerchantDataSuccess()
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
                    .padding(bottomPadding)
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

            val scope = rememberCoroutineScope()

            LazyColumn(
                state = scrollState,
                modifier = modifier
                    .fillMaxSize()
                    .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                    .padding(innerPadding)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_inner_padding)),
                contentPadding = bottomPadding
            ) {
                items(count = lazyPagingData.itemCount,
                    key = lazyPagingData.itemKey { item -> item.id }
                ) { index ->
                    val itemAnimateScale = remember {
                        Animatable(0f)
                    }

                    val itemAnimateScaleDown = remember {
                        Animatable(1f)
                    }

                    val baseColor = MyAppTheme.colors.itemBg
                    val targetAnimColor = MyAppTheme.colors.lightBlue1

                    val itemAnimateColor = remember {
                        Animatable(baseColor)
                    }

                    val data = lazyPagingData[index]
                    if (data != null) {

                        val modifierAdd: Modifier =
                            if (editAddId == data.id && merchantViewModel.addAnimRun.value) {
                                scope.launch {
                                    itemAnimateScale.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(50)
                                    )
                                }
                                Modifier.scale(itemAnimateScale.value)
                            } else if (merchantViewModel.deleteAnimRun.value &&
                                selectedList.contains(data.id)
                            ) {
                                scope.launch {
                                    itemAnimateScaleDown.animateTo(
                                        targetValue = 0.0f,
                                        animationSpec = tween(50)
                                    )
                                }
                                Modifier.scale(itemAnimateScaleDown.value)
                            } else if ((editAddId == data.id && merchantViewModel.editAnimRun.value) ||
                                (merchantId == data.id && merchantViewModel.addDataAnimRun.value)
                            ) {
                                scope.launch {
                                    itemAnimateColor.animateTo(
                                        targetValue = targetAnimColor,
                                        animationSpec = tween()
                                    )
                                    itemAnimateColor.animateTo(
                                        targetValue = baseColor,
                                        animationSpec = tween()
                                    )
                                }
                                Modifier
                            } else {
                                Modifier
                            }

                        MerchantListItem(
                            item = data,
                            isSelected = selectedList.contains(data.id),
                            onClick = { merchantViewModel.onItemClick(data.id) { onMerchantClick(it) } },
                            onLongClick = { merchantViewModel.onItemLongClick(data.id) },
                            modifier = modifierAdd,
                            itemBgColor = itemAnimateColor.value
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
    PennyPalTheme(darkTheme = true) {
        MerchantScreen(
            onAddClick = {},
            onEditClick = {},
            onMerchantClick = {},
            bottomPadding = PaddingValues(0.dp)
        )
    }
}