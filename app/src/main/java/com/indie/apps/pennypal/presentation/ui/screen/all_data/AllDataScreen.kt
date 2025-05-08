package com.indie.apps.pennypal.presentation.ui.screen.all_data

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.indie.apps.pennypal.presentation.ui.component.composable.common.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.SearchView
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.addAnim
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.editAnim
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.screen.AdViewModel
import com.indie.apps.pennypal.presentation.ui.screen.InAppFeedbackViewModel
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.AnimationType
import com.indie.apps.pennypal.util.app_enum.DialogType
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AllDataScreen(
    allDataViewModel: AllDataViewModel = hiltViewModel(),
    inAppFeedbackViewModel: InAppFeedbackViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel(),
    onDataClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onNavigationUp: () -> Unit,
    isEditSuccess: Boolean = false,
    isAddSuccess: Boolean = false,
    isDeleteSuccess: Boolean = false,
    editAddId: Long = -1L,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Load ad when screen is created
    LaunchedEffect(Unit) {
        adViewModel.loadInterstitialAd()
    }

    val merchantDeleteToast = stringResource(id = R.string.data_delete_success_message)
    val lazyPagingData = allDataViewModel.pagedData.collectAsLazyPagingItems()
    val pagingState by allDataViewModel.pagingState.collectAsStateWithLifecycle()
    pagingState.update(lazyPagingData)

    val searchTextState by allDataViewModel.searchTextState.collectAsStateWithLifecycle()
    val isDeletable by allDataViewModel.isDeletable.collectAsStateWithLifecycle()
    val scrollOffset by allDataViewModel.scrollOffset.collectAsStateWithLifecycle()
    val scrollIndex by allDataViewModel.scrollIndex.collectAsStateWithLifecycle()
    val selectedList = allDataViewModel.selectedList

    val currentAnim by allDataViewModel.currentAnim.collectAsStateWithLifecycle()
    val merchantAnimId by allDataViewModel.merchantAnimId.collectAsStateWithLifecycle()

    LaunchedEffect(editAddId) {
        if (isAddSuccess) {
            allDataViewModel.addDataSuccess(editAddId)
            inAppFeedbackViewModel.triggerReview(context)
        } else if (isEditSuccess) {
            allDataViewModel.editDataSuccess(editAddId)
        } else if (isDeleteSuccess) {
            allDataViewModel.onDeleteFromEditScreenClick(editAddId) {
                context.showToast(merchantDeleteToast)
            }
        }
    }

    BackHandler {
        allDataViewModel.onBackClick {
            onNavigationUp()
        }
    }


    var openDialog by remember { mutableStateOf<DialogType?>(null) }

    var job: Job? = null
    Scaffold(topBar = {
        AllDataTopBar(
            title = if (selectedList.size > 0) "${selectedList.size} " + stringResource(
                id = R.string.selected_text
            ) else stringResource(R.string.transactions),
            textState = searchTextState,
            isSelected = allDataViewModel.getIsSelected(),
            isDeletable = isDeletable,
            onAddClick = { allDataViewModel.onAddClick { onAddClick() } },
            onDeleteClick = { allDataViewModel.onDeleteClick { openDialog = DialogType.Delete } },
            onNavigationUp = {
                allDataViewModel.onBackClick { onNavigationUp() }
            },
            onSearchTextChange = {
                allDataViewModel.updateSearchText(it)
                job?.cancel()
                job = MainScope().launch {
                    delay(Util.SEARCH_NEWS_TIME_DELAY)
                    allDataViewModel.searchData()
                }
            })
    }) { innerPadding ->

        Column(
            modifier = Modifier
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                //.padding(bottomPadding)
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        )

        {
            SearchView(
                textState = searchTextState,
                onTextChange = {
                    allDataViewModel.updateSearchText(it)
                    job?.cancel()
                    job = MainScope().launch {
                        delay(Util.SEARCH_NEWS_TIME_DELAY)
                        allDataViewModel.searchData()
                    }
                },
                trailingIcon = Icons.Default.Search,
                bgColor = MyAppTheme.colors.lightBlue2,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.top_bar_profile)),
                paddingValues = PaddingValues(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = dimensionResource(id = R.dimen.padding),
                    end = 0.dp
                )
            )
            Spacer(Modifier.height(dimensionResource((R.dimen.padding))))

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

                LaunchedEffect(lazyPagingData.itemCount) {
                    if (currentAnim == AnimationType.ADD)
                        scrollState.scrollToItem(0, 0)
                }

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
                ) {
                    items(
                        count = lazyPagingData.itemCount,
                        key = lazyPagingData.itemKey { item -> item.id }
                    ) { index ->
                        val baseColor = MyAppTheme.colors.itemBg

                        val itemAnimateColor = remember {
                            Animatable(baseColor)
                        }

                        val data = lazyPagingData[index]
                        if (data != null) {

                            val modifierAnim = if (merchantAnimId == data.id) {
                                when (currentAnim) {
                                    AnimationType.ADD -> Modifier.addAnim(scope) {
                                        allDataViewModel.onAnimationComplete(
                                            AnimationType.ADD
                                        )
                                    }

                                    AnimationType.EDIT -> Modifier.editAnim(
                                        scope,
                                        itemAnimateColor
                                    ) { allDataViewModel.onAnimationComplete(AnimationType.EDIT) }

                                    else -> Modifier
                                }
                            } else Modifier

                            var visible by remember {
                                mutableStateOf(true)
                            }

                            if (currentAnim == AnimationType.DELETE &&
                                (selectedList.contains(data.id) || merchantAnimId == data.id)
                            ) {
                                visible = false
                            }

                            AnimatedVisibility(
                                visible = visible,
                                exit = slideOutVertically() + shrinkVertically() + fadeOut()
                            ) {
                                TransactionItem(
                                    item = data,
                                    isSelected = selectedList.contains(data.id),
                                    onClick = {
                                        allDataViewModel.onItemClick(data.id) {
                                            adViewModel.showInterstitialAd(context as android.app.Activity) {
                                                onDataClick(
                                                    it
                                                )
                                            }
                                        }
                                    },
                                    onLongClick = { allDataViewModel.onItemLongClick(data.id) },
                                    modifier = modifierAnim,
                                    itemBgColor = itemAnimateColor.value,
                                    // currency = currency
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

            // Dialog Handling
            openDialog?.let { dialog ->
                when (dialog) {
                    DialogType.Delete -> ConfirmationDialog(
                        dialogTitle = R.string.delete_dialog_title,
                        dialogText = R.string.delete_item_dialog_text,
                        onConfirmation = {
                            allDataViewModel.onDeleteDialogClick {
                                openDialog = null
                                context.showToast(merchantDeleteToast)
                            }
                        },
                        onDismissRequest = { openDialog = null }
                    )

                    else -> {}
                }
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
        )
    }
}