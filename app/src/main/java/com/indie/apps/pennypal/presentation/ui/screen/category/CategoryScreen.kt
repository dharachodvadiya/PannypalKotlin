package com.indie.apps.pennypal.presentation.ui.screen.category

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.viewinterop.AndroidView
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
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.ads.AdViewModel
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
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel(),
    onNavigationUp: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    isEditSuccess: Boolean = false,
    isAddSuccess: Boolean = false,
    isAddMerchantDataSuccess: Boolean = false,
    merchantEditAddId: Long = -1L,
    merchantDataMerchantId: Long = -1L,
    bottomPadding: PaddingValues,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Load ad when screen is created
    LaunchedEffect(Unit) {
        adViewModel.loadInterstitialAd()
    }

    val categoryDeleteToast = stringResource(id = R.string.category_delete_success_message)
    val lazyPagingData = viewModel.pagedData.collectAsLazyPagingItems()
    val pagingState by viewModel.pagingState.collectAsStateWithLifecycle()
    pagingState.update(lazyPagingData)

    val searchTextState by viewModel.searchTextState.collectAsStateWithLifecycle()
    val scrollOffset by viewModel.scrollOffset.collectAsStateWithLifecycle()
    val scrollIndex by viewModel.scrollIndex.collectAsStateWithLifecycle()

    val currentAnim by viewModel.currentAnim.collectAsStateWithLifecycle()
    val currentAnimId by viewModel.currentAnimId.collectAsStateWithLifecycle()


    BackHandler {
        viewModel.onBackClick { onNavigationUp() }
    }

    LaunchedEffect(merchantEditAddId) {
        if (isAddSuccess) {
            viewModel.addCategorySuccess(merchantEditAddId)
        }
        if (isEditSuccess) {
            viewModel.editCategorySuccess(merchantEditAddId)
        }
    }

    /* LaunchedEffect(merchantDataMerchantId) {
         if (isAddMerchantDataSuccess) {
             viewModel.addMerchantDataSuccess()
         }
     }
 */
    var openDialog by remember { mutableStateOf<DialogType?>(null) }
    var deleteId by remember { mutableLongStateOf(-1L) }

    var job: Job? = null
    Scaffold(topBar = {
        CategoryTopBar(
            title = stringResource(R.string.category),
            onAddClick = { viewModel.onAddClick { onAddClick() } },
            onNavigationUp = {
                viewModel.onBackClick { onNavigationUp() }
            })
    }) { topBarPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                //.padding(bottomPadding)
                .padding(top = topBarPadding.calculateTopPadding())
                .padding(horizontal = dimensionResource(id = R.dimen.padding)),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        )
        {

            val isBannerVisibleFlow = remember { mutableStateOf(false) }
            val bannerAdViewFlow by remember {
                mutableStateOf(
                    adViewModel.loadBannerAd() { adState ->
                        isBannerVisibleFlow.value = adState.bannerAdView != null
                    }
                )
            }


            AnimatedVisibility(
                visible = isBannerVisibleFlow.value,
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg)),
                    factory = { bannerAdViewFlow }
                )
            }
            SearchView(
                textState = searchTextState,
                onTextChange = {
                    viewModel.updateSearchText(it)
                    job?.cancel()
                    job = MainScope().launch {
                        delay(Util.SEARCH_NEWS_TIME_DELAY)
                        viewModel.searchData()
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
                        viewModel.setScrollVal(
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

                            val modifierAnim = if (currentAnimId == data.id) {
                                when (currentAnim) {
                                    AnimationType.ADD -> Modifier.addAnim(scope) {
                                        viewModel.onAnimationComplete(
                                            AnimationType.ADD
                                        )
                                    }

                                    AnimationType.EDIT -> Modifier.editAnim(
                                        scope,
                                        itemAnimateColor
                                    ) { viewModel.onAnimationComplete(AnimationType.EDIT) }

                                    else -> Modifier
                                }
                            } else Modifier


                            var visible by remember {
                                mutableStateOf(true)
                            }

                            if (currentAnim == AnimationType.DELETE &&
                                deleteId == data.id
                            ) {
                                visible = false
                            }

                            AnimatedVisibility(
                                visible = visible,
                                exit = slideOutVertically() + shrinkVertically() + fadeOut()
                            )

                            {
                                CategoryListItem(
                                    item = data,
                                    isSelected = false,
                                    onClick = {
                                        adViewModel.showInterstitialAd(context as android.app.Activity) {
                                            onEditClick(data.id)
                                        }
                                    },
                                    onDeleteClick = {
                                        deleteId = data.id
                                        openDialog = DialogType.Delete
                                    },
                                    onLongClick = { },
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

            openDialog?.let { dialog ->
                when (dialog) {
                    DialogType.Delete -> {
                        ConfirmationDialog(
                            dialogTitle = R.string.delete_dialog_title,
                            dialogText = R.string.delete_item_dialog_text,
                            onConfirmation = {
                                viewModel.onDeleteDialogClick(deleteId) {
                                    deleteId = -1
                                    openDialog = null
                                    context.showToast(categoryDeleteToast)
                                }
                            },
                            onDismissRequest = {
                                deleteId = -1
                                openDialog = null
                            }
                        )
                    }

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
        CategoryScreen(
            onAddClick = {},
            onEditClick = {},
            onNavigationUp = {},
            bottomPadding = PaddingValues(0.dp)
        )
    }
}