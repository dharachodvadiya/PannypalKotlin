package com.indie.apps.pennypal.presentation.ui.screen.merchant

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import com.indie.apps.pennypal.data.database.entity.toMerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.SearchView
import com.indie.apps.pennypal.presentation.ui.component.showToast
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
fun MerchantScreen(
    merchantViewModel: MerchantViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onMerchantClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    isEditSuccess: Boolean = false,
    isAddSuccess: Boolean = false,
    isAddMerchantDataSuccess: Boolean = false,
    editAddId: Long = -1L,
    merchantId: Long = -1L,
    bottomPadding: PaddingValues,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
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

    val addAnimRun by merchantViewModel.addAnimRun.collectAsStateWithLifecycle()
    val addDataAnimRun by merchantViewModel.addDataAnimRun.collectAsStateWithLifecycle()
    val editAnimRun by merchantViewModel.editAnimRun.collectAsStateWithLifecycle()
    val deleteAnimRun by merchantViewModel.deleteAnimRun.collectAsStateWithLifecycle()

    var addMerchantId by remember {
        mutableLongStateOf(-1L)
    }
    var editMerchantId by remember {
        mutableLongStateOf(-1L)
    }

    BackHandler {
        merchantViewModel.onBackClick {
            onNavigationUp()
        }
    }

    LaunchedEffect(editAddId) {
        if (isAddSuccess) {
            addMerchantId = editAddId
            merchantViewModel.addMerchantSuccess()
        }
        if (isEditSuccess) {
            editMerchantId = editAddId
            merchantViewModel.editMerchantSuccess()
        }
    }

    var addMerchantDataId by remember {
        mutableLongStateOf(-1L)
    }

    LaunchedEffect(merchantId) {
        if (isAddMerchantDataSuccess) {
            addMerchantDataId = merchantId
            merchantViewModel.addMerchantDataSuccess()
        }
    }

    var openAlertDialog by remember { mutableStateOf(false) }

    var job: Job? = null
    Scaffold(topBar = {
        MerchantTopBar(
            title = if (selectedList.size > 0) "${selectedList.size} " + stringResource(
                id = R.string.selected_text
            ) else stringResource(R.string.merchants),
            textState = searchTextState,
            isSelected = merchantViewModel.getIsSelected(),
            isEditable = isEditable,
            isDeletable = isDeletable,
            onAddClick = { merchantViewModel.onAddClick { onAddClick() } },
            onEditClick = { merchantViewModel.onEditClick { onEditClick(it) } },
            onDeleteClick = { merchantViewModel.onDeleteClick { openAlertDialog = true } },
            onNavigationUp = {
                merchantViewModel.onBackClick {
                    onNavigationUp()
                }
            },
            onSearchTextChange = {
                merchantViewModel.updateSearchText(it)
                job?.cancel()
                job = MainScope().launch {
                    delay(Util.SEARCH_NEWS_TIME_DELAY)
                    merchantViewModel.searchData()
                }
            })
    }) { topBarPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                //.padding(bottomPadding)
                .padding(top = topBarPadding.calculateTopPadding())
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
        ) {
            SearchView(
                textState = searchTextState,
                onTextChange = {
                    merchantViewModel.updateSearchText(it)
                    job?.cancel()
                    job = MainScope().launch {
                        delay(Util.SEARCH_NEWS_TIME_DELAY)
                        merchantViewModel.searchData()
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
                    title = stringResource(id = R.string.no_merchants),
                    details = stringResource(id = R.string.no_merchants_details_with_transaction),
                    iconSize = 70.dp,
                    painterRes = R.drawable.person_off
                )
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
                        .fillMaxSize(),
                    //verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.item_inner_padding)),
                    contentPadding = PaddingValues(bottom = bottomPadding.calculateBottomPadding())
                ) {
                    items(count = lazyPagingData.itemCount,
                        key = lazyPagingData.itemKey { item -> item.id }
                    ) { index ->
                        val itemAnimateScale = remember {
                            Animatable(0f)
                        }

                        /*val itemAnimateScaleDown = remember {
                            Animatable(1f)
                        }*/

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
                                        merchantViewModel.addMerchantSuccessAnimStop()
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
                            }*/ else if ((editMerchantId == data.id && editAnimRun) ||
                                    (addMerchantDataId == data.id && addDataAnimRun)
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
                                MerchantListItem(
                                    item = data.toMerchantNameAndDetails(),
                                    isSelected = selectedList.contains(data.id),
                                    onClick = {
                                        merchantViewModel.onItemClick(data.id) {
                                            onMerchantClick(
                                                it
                                            )
                                        }
                                    },
                                    onLongClick = { merchantViewModel.onItemLongClick(data.id) },
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
                        merchantViewModel.onDeleteDialogClick {
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
        MerchantScreen(
            onAddClick = {},
            onEditClick = {},
            onMerchantClick = {},
            onNavigationUp = {},
            bottomPadding = PaddingValues(0.dp)
        )
    }
}