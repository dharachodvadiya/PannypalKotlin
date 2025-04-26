package com.indie.apps.pennypal.presentation.ui.screen.merchant_data

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.toMerchantNameAndDetails
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.screen.InAppFeedbackViewModel
import com.indie.apps.pennypal.presentation.ui.screen.loading.LoadingWithProgress
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util
import com.indie.apps.pennypal.util.getDateFromMillis
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MerchantDataScreen(
    merchantDataViewModel: MerchantDataViewModel = hiltViewModel(),
    inAppFeedbackViewModel: InAppFeedbackViewModel = hiltViewModel(),
    onProfileClick: (Long) -> Unit,
    onNavigationUp: () -> Unit,
    onEditClick: (Long) -> Unit,
    onAddClick: (MerchantNameAndDetails) -> Unit,
    isEditSuccess: Boolean = false,
    isAddSuccess: Boolean = false,
    merchantDataId: Long = -1L,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    // val currency by merchantDataViewModel.currency.collectAsStateWithLifecycle()
    val merchant by merchantDataViewModel.merchantState.collectAsStateWithLifecycle()
    val selectedList = merchantDataViewModel.selectedList
    val scrollIndex by merchantDataViewModel.scrollIndex.collectAsStateWithLifecycle()
    val scrollOffset by merchantDataViewModel.scrollOffset.collectAsStateWithLifecycle()
    val isEditable by merchantDataViewModel.isEditable.collectAsStateWithLifecycle()
    val isDeletable by merchantDataViewModel.isDeletable.collectAsStateWithLifecycle()
    val addDataAnimRun by merchantDataViewModel.addDataAnimRun.collectAsStateWithLifecycle()
    val editDataAnimRun by merchantDataViewModel.editDataAnimRun.collectAsStateWithLifecycle()
    val deleteAnimRun by merchantDataViewModel.deleteAnimRun.collectAsStateWithLifecycle()

    val lazyPagingData = merchantDataViewModel.pagedData.collectAsLazyPagingItems()
    val pagingState by merchantDataViewModel.pagingState.collectAsStateWithLifecycle()
    pagingState.update(lazyPagingData)

    var openAlertDialog by remember { mutableStateOf(false) }

    var addEditMerchantDataId by remember {
        mutableLongStateOf(-1L)
    }

    LaunchedEffect(merchantDataId) {
        if (isEditSuccess) {
            addEditMerchantDataId = merchantDataId
            merchantDataViewModel.editMerchantDataSuccess()
        }
        if (isAddSuccess) {
            addEditMerchantDataId = merchantDataId
            merchantDataViewModel.addMerchantDataSuccess()
            inAppFeedbackViewModel.triggerReview(context)
        }
    }

    Scaffold(
        topBar = {
            MerchantDataTopBar(
                selectCount = selectedList.size,
                name = merchant?.name ?: "",
                description = merchant?.details ?: "",
                onClick = { merchant?.let { onProfileClick(it.id) } },
                onAddClick = { merchant?.let { onAddClick(it.toMerchantNameAndDetails()) } },
                onNavigationUp = onNavigationUp,
                onCloseClick = {
                    merchantDataViewModel.clearSelection()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(padding)
        ) {

            if (pagingState.isRefresh && (lazyPagingData.itemCount == 0)) {
                LoadingWithProgress(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MyAppTheme.colors.transparent)
                )
            } else if (lazyPagingData.itemCount == 0) {
                NoDataMessage(
                    title = stringResource(id = R.string.no_transactions),
                    details = "",
                    iconSize = 70.dp,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {

                val scrollState: LazyListState = rememberLazyListState(
                    scrollIndex,
                    scrollOffset
                )

                LaunchedEffect(lazyPagingData.itemCount) {
                    if (addDataAnimRun)
                        scrollState.scrollToItem(0, 0)
                }

                // after each scroll, update values in ViewModel
                LaunchedEffect(key1 = scrollState.isScrollInProgress) {
                    if (!scrollState.isScrollInProgress) {

                        merchantDataViewModel.setScrollVal(
                            scrollIndex = scrollState.firstVisibleItemIndex,
                            scrollOffset = scrollState.firstVisibleItemScrollOffset
                        )
                    }
                }

                val scope = rememberCoroutineScope()

                LazyColumn(
                    reverseLayout = true,
                    state = scrollState,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(
                        count = lazyPagingData.itemCount,
                        key = lazyPagingData.itemKey { item -> item.id }
                    ) { index ->

                        val itemAnimateScale = remember {
                            androidx.compose.animation.core.Animatable(0f)
                        }

                        val baseColor = MyAppTheme.colors.itemBg
                        val targetAnimColor = MyAppTheme.colors.lightBlue1

                        val itemAnimateColor = remember {
                            Animatable(baseColor)
                        }

                        val data = lazyPagingData[index]
                        if (data != null) {

                            val modifierAdd: Modifier =
                                if (addEditMerchantDataId == data.id && addDataAnimRun) {
                                    scope.launch {
                                        itemAnimateScale.animateTo(
                                            targetValue = 1f,
                                            animationSpec = tween(Util.ADD_ITEM_ANIM_TIME)
                                        )
                                    }
                                    if (itemAnimateScale.value == 1f) {
                                        merchantDataViewModel.addMerchantSuccessAnimStop()
                                    }
                                    Modifier.scale(itemAnimateScale.value)
                                } else if (addEditMerchantDataId == data.id && editDataAnimRun) {
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
                                } /*else if (deleteAnimRun &&
                                    selectedList.contains(data.id)
                                ) {
                                    scope.launch {
                                        itemAnimateScaleDown.animateTo(
                                            targetValue = 0.0f,
                                            animationSpec = tween(50),
                                        )
                                    }
                                    if(itemAnimateScaleDown.value < 0.05)
                                    {
                                        merchantDataViewModel.onDeleteAnimStop()
                                    }
                                    Modifier.scale(itemAnimateScaleDown.value)
                                }*/ else {
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

                            val currentDate = getDateFromMillis(data.dateInMilli)
                            val previousDate =
                                if (index != lazyPagingData.itemCount - 1) lazyPagingData[index + 1]?.let {
                                    getDateFromMillis(
                                        it.dateInMilli
                                    )
                                } else ""

                            AnimatedVisibility(
                                visible = visible,
                                exit = slideOutVertically() + shrinkVertically() + fadeOut()
                            )
                            {
                                if (data.type >= 0) {
                                    MerchantDataIncomeAmount(
                                        isSelected = selectedList.contains(data.id),
                                        data = data,
                                        onClick = { merchantDataViewModel.onItemClick(data.id) },
                                        onLongClick = { merchantDataViewModel.onItemLongClick(data.id) },
                                        itemBgColor = itemAnimateColor.value,
                                        modifier = modifierAdd
                                    )
                                } else {
                                    MerchantDataExpenseAmount(
                                        isSelected = selectedList.contains(data.id),
                                        data = data,
                                        onClick = { merchantDataViewModel.onItemClick(data.id) },
                                        onLongClick = { merchantDataViewModel.onItemLongClick(data.id) },
                                        itemBgColor = itemAnimateColor.value,
                                        modifier = modifierAdd
                                    )
                                }
                            }

                            if (currentDate != previousDate) {
                                MerchantDataDateItem(
                                    dateMillis = data.dateInMilli
                                )
                            }
                        }

                        if (pagingState.isLoadMore && index == lazyPagingData.itemCount) {
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
            MerchantDataBottomBar(
                //totalIncome = merchant?.incomeAmount ?: 0.0,
                //totalExpense = merchant?.expenseAmount ?: 0.0,
                isEditable = isEditable,
                isDeletable = isDeletable,
                onEditClick = { merchantDataViewModel.onEditClick(onSuccess = onEditClick) },
                onDeleteClick = { openAlertDialog = true },
                onAddClick = { merchant?.let { onAddClick(it.toMerchantNameAndDetails()) } }
            )

        }
        val context = LocalContext.current
        val merchantDataDeleteToast =
            stringResource(id = R.string.merchant_data_delete_success_message)

        if (openAlertDialog) {
            ConfirmationDialog(
                dialogTitle = R.string.delete_dialog_title,
                dialogText = R.string.delete_item_dialog_text,
                onConfirmation = {
                    merchantDataViewModel.onDeleteDialogClick {
                        openAlertDialog = false
                        context.showToast(merchantDataDeleteToast)
                    }
                },
                onDismissRequest = { openAlertDialog = false }
            )
        }
    }
}

@Preview
@Composable
private fun MerchantDataScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        MerchantDataScreen(
            onProfileClick = {},
            onNavigationUp = {},
            onEditClick = {},
            onAddClick = {}
        )
    }
}