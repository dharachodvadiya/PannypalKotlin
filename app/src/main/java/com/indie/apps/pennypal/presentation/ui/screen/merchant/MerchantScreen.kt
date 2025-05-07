package com.indie.apps.pennypal.presentation.ui.screen.merchant

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
import androidx.compose.runtime.mutableLongStateOf
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
import com.indie.apps.pennypal.data.database.db_entity.toMerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.composable.common.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.SearchView
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.addAnim
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.editAnim
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.screen.InAppFeedbackViewModel
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.app_enum.AnimationType
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MerchantScreen(
    merchantViewModel: MerchantViewModel = hiltViewModel(),
    inAppFeedbackViewModel: InAppFeedbackViewModel = hiltViewModel(),
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

    val currentAnim by merchantViewModel.currentAnim.collectAsStateWithLifecycle()
    val currentAnimId by merchantViewModel.currentAnimId.collectAsStateWithLifecycle()

    BackHandler {
        merchantViewModel.onBackClick {
            onNavigationUp()
        }
    }

    LaunchedEffect(editAddId) {
        if (isAddSuccess) {
            merchantViewModel.addMerchantSuccess(editAddId)
            inAppFeedbackViewModel.triggerReview(context)
        }
        if (isEditSuccess) {
            merchantViewModel.editMerchantSuccess(editAddId)
        }
    }

    var addMerchantDataId by remember {
        mutableLongStateOf(-1L)
    }

    /*LaunchedEffect(merchantId) {
        if (isAddMerchantDataSuccess) {
            addMerchantDataId = merchantId
            merchantViewModel.addMerchantDataSuccess()
        }
    }*/

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

                            val modifierAnim = if (currentAnimId == data.id) {
                                when (currentAnim) {
                                    AnimationType.ADD -> Modifier.addAnim(scope) {
                                        merchantViewModel.onAnimationComplete(
                                            AnimationType.ADD
                                        )
                                    }

                                    AnimationType.EDIT -> Modifier.editAnim(
                                        scope,
                                        itemAnimateColor
                                    ) { merchantViewModel.onAnimationComplete(AnimationType.EDIT) }

                                    else -> Modifier
                                }
                            } else Modifier

                            var visible by remember {
                                mutableStateOf(true)
                            }

                            if (currentAnim == AnimationType.DELETE &&
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
                                    modifier = modifierAnim,
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