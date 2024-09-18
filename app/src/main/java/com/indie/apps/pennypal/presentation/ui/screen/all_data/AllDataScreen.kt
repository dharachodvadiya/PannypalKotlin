package com.indie.apps.pennypal.presentation.ui.screen.all_data

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableLongStateOf
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
import com.indie.apps.pennypal.presentation.ui.component.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AllDataScreen(
    allDataViewModel: AllDataViewModel = hiltViewModel(),
    onDataClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onNavigationUp: () -> Unit,
    isEditSuccess: Boolean = false,
    isAddSuccess: Boolean = false,
    editAddId: Long = 1L,
    bottomPadding: PaddingValues,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val merchantDeleteToast = stringResource(id = R.string.data_delete_success_message)
    val lazyPagingData = allDataViewModel.pagedData.collectAsLazyPagingItems()
    val pagingState by allDataViewModel.pagingState.collectAsStateWithLifecycle()
    pagingState.update(lazyPagingData)

    val searchTextState by allDataViewModel.searchTextState.collectAsStateWithLifecycle()
    val isDeletable by allDataViewModel.isDeletable.collectAsStateWithLifecycle()
    val scrollOffset by allDataViewModel.scrollOffset.collectAsStateWithLifecycle()
    val scrollIndex by allDataViewModel.scrollIndex.collectAsStateWithLifecycle()
    val selectedList = allDataViewModel.selectedList

    val addAnimRun by allDataViewModel.addAnimRun.collectAsStateWithLifecycle()
    val editAnimRun by allDataViewModel.editAnimRun.collectAsStateWithLifecycle()
    val deleteAnimRun by allDataViewModel.deleteAnimRun.collectAsStateWithLifecycle()

    var isAddMerchantSuccessState by remember {
        mutableStateOf(false)
    }

    var addMerchantId by remember {
        mutableLongStateOf(-1L)
    }

    if (isAddMerchantSuccessState != isAddSuccess) {
        if (isAddSuccess) {
            addMerchantId = editAddId
            allDataViewModel.addDataSuccess()
        }
        isAddMerchantSuccessState = isAddSuccess
    }
    var isEditMerchantSuccessState by remember {
        mutableStateOf(false)
    }

    var editMerchantId by remember {
        mutableLongStateOf(-1L)
    }

    if (isEditMerchantSuccessState != isEditSuccess) {
        if (isEditSuccess) {
            editMerchantId = editAddId
            allDataViewModel.editDataSuccess()
        }
        isEditMerchantSuccessState = isEditSuccess
    }

    var openAlertDialog by remember { mutableStateOf(false) }

    var job: Job? = null
    Scaffold(topBar = {
        AllDataTopBar(
            title = if (selectedList.size > 0) "${selectedList.size} " + stringResource(
                id = R.string.selected_text
            ) else "",
            textState = searchTextState,
            isDeletable = isDeletable,
            onAddClick = { allDataViewModel.onAddClick { onAddClick() } },
            onDeleteClick = { allDataViewModel.onDeleteClick { openAlertDialog = true } },
            onNavigationUp = { allDataViewModel.onNavigationUp {
                if(!isDeletable)
                    onNavigationUp()
            } },
            onSearchTextChange = {
                job?.cancel()
                job = MainScope().launch {
                    delay(Util.SEARCH_NEWS_TIME_DELAY)
                    allDataViewModel.searchData()
                }
            })
    }) { innerPadding ->

        Box(
            modifier = Modifier
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                //.padding(bottomPadding)
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        )

        {
            if (pagingState.isRefresh && (lazyPagingData.itemCount == 0 || isAddSuccess)) {
                LoadingWithProgress(
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else if (lazyPagingData.itemCount == 0) {
                NoDataMessage(
                    title = stringResource(id = R.string.no_transaction),
                    details = stringResource(id = R.string.no_data_with_transaction),
                    iconSize = 70.dp
                )
            } else {

                val scrollState: LazyListState = rememberLazyListState(
                    scrollIndex,
                    scrollOffset
                )

                // after each scroll, update values in ViewModel
                LaunchedEffect(key1 = scrollState.isScrollInProgress) {
                    if (!scrollState.isScrollInProgress) {
                        allDataViewModel.setScrollVal(
                            scrollIndex = scrollState.firstVisibleItemIndex,
                            scrollOffset = scrollState.firstVisibleItemScrollOffset
                        )
                    }
                }

                val scope = rememberCoroutineScope()

                LazyColumn(
                    state = scrollState,
                    modifier = modifier
                        .fillMaxSize(),
                    //verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_inner_padding)),
                    contentPadding = bottomPadding
                ) {
                    items(count = lazyPagingData.itemCount,
                        key = lazyPagingData.itemKey { item -> item.id }
                    ) { index ->
                        val itemAnimateScale = remember {
                            Animatable(0f)
                        }

                        val baseColor = MyAppTheme.colors.itemBg
                        val targetAnimColor = MyAppTheme.colors.lightBlue1

                        val itemAnimateColor = remember {
                            Animatable(baseColor)
                        }

                        val data = lazyPagingData[index]
                        if (data != null) {

                            val modifierAdd: Modifier =
                                if (addMerchantId == data.id && addAnimRun) {
                                    scope.launch {
                                        itemAnimateScale.animateTo(
                                            targetValue = 1f,
                                            animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
                                        )
                                    }
                                    if (itemAnimateScale.value == 1f) {
                                        allDataViewModel.addDataSuccessAnimStop()
                                    }
                                    Modifier.scale(itemAnimateScale.value)
                                }/* else if (deleteAnimRun &&
                                selectedList.contains(data.id)
                            ) {
                                scope.launch {
                                    itemAnimateScaleDown.animateTo(
                                        targetValue = 0.0f,
                                        animationSpec = tween(50)
                                    )
                                }
                                if (itemAnimateScaleDown.value < 0.02) {
                                    merchantViewModel.onDeleteAnimStop()
                                }
                                Modifier.scale(itemAnimateScaleDown.value)
                            }*/ else if ((editMerchantId == data.id && editAnimRun)
                                ) {
                                    scope.launch {
                                        itemAnimateColor.animateTo(
                                            targetValue = targetAnimColor,
                                            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                                        )
                                        itemAnimateColor.animateTo(
                                            targetValue = baseColor,
                                            animationSpec = tween(Util.EDIT_ITEM_ANIM_TIME)
                                        )
                                    }
                                    Modifier
                                } else {
                                    Modifier
                                }

                            var visible by remember {
                                mutableStateOf(true)
                            }

                            if (deleteAnimRun &&
                                selectedList.contains(data.id)
                            ) {
                                visible = false
                            }

                            AnimatedVisibility(
                                visible = visible,
                                exit = slideOutVertically() + shrinkVertically() + fadeOut()
                            )

                            {
                                DataListItem(
                                    item = data,
                                    isSelected = selectedList.contains(data.id),
                                    onClick = {
                                        allDataViewModel.onItemClick(data.id) {
                                            onDataClick(
                                                it
                                            )
                                        }
                                    },
                                    onLongClick = { allDataViewModel.onItemLongClick(data.id) },
                                    modifier = modifierAdd,
                                    itemBgColor = itemAnimateColor.value
                                )
                            }

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
                ConfirmationDialog(
                    dialogTitle = R.string.delete_dialog_title,
                    dialogText = R.string.delete_item_dialog_text,
                    onConfirmation = {
                        allDataViewModel.onDeleteDialogClick {
                            openAlertDialog = false
                            context.showToast(merchantDeleteToast)
                       }
                    },
                    onDismissRequest = { openAlertDialog = false }
                )
            }
        }


    }
}

@Preview
@Composable
private fun MerchantScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        AllDataScreen(
            onAddClick = {},
            onDataClick = {},
            onNavigationUp = {},
            bottomPadding = PaddingValues(0.dp)
        )
    }
}