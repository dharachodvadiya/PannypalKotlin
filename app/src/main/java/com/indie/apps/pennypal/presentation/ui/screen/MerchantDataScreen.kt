package com.indie.apps.pennypal.presentation.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.DeleteAlertDialog
import com.indie.apps.pennypal.presentation.ui.component.NoDataMessage
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.screen.MerchantDataBottomBar
import com.indie.apps.pennypal.presentation.ui.component.screen.MerchantDataDateItem
import com.indie.apps.pennypal.presentation.ui.component.screen.MerchantDataExpenseAmount
import com.indie.apps.pennypal.presentation.ui.component.screen.MerchantDataIncomeAmount
import com.indie.apps.pennypal.presentation.ui.component.screen.MerchantDataTopBar
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.presentation.viewmodel.MerchantDataViewModel
import com.indie.apps.pennypal.util.Util
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MerchantDataScreen(
    merchantDataViewModel: MerchantDataViewModel = hiltViewModel(),
    onProfileClick: (Long) -> Unit,
    onNavigationUp: () -> Unit,
    onEditClick: (Long) -> Unit,
    isEditMerchantDataSuccess: Boolean = false,
    merchantDataId: Long = 1L,
    modifier: Modifier = Modifier
) {

    val merchant by merchantDataViewModel.merchantState.collectAsStateWithLifecycle()
    val selectedList = merchantDataViewModel.selectedList
    val scrollIndex by merchantDataViewModel.scrollIndex.collectAsStateWithLifecycle()
    val scrollOffset by merchantDataViewModel.scrollOffset.collectAsStateWithLifecycle()
    val isEditable by merchantDataViewModel.isEditable.collectAsStateWithLifecycle()
    val isDeletable by merchantDataViewModel.isDeletable.collectAsStateWithLifecycle()
    val editDataAnimRun by merchantDataViewModel.editDataAnimRun.collectAsStateWithLifecycle()
    val deleteAnimRun by merchantDataViewModel.deleteAnimRun.collectAsStateWithLifecycle()

    val lazyPagingData = merchantDataViewModel.pagedData.collectAsLazyPagingItems()
    val pagingState by merchantDataViewModel.pagingState.collectAsStateWithLifecycle()
    pagingState.update(lazyPagingData)

    var openAlertDialog by remember { mutableStateOf(false) }

    var isEditMerchantDataSuccessState by remember {
        mutableStateOf(false)
    }

    var editMerchantId by remember {
        mutableStateOf(-1L)
    }

    if (isEditMerchantDataSuccessState != isEditMerchantDataSuccess) {
        if (isEditMerchantDataSuccess) {
            editMerchantId = merchantDataId
            merchantDataViewModel.editMerchantDataSuccess()
        }
        isEditMerchantDataSuccessState = isEditMerchantDataSuccess
    }

    /*LaunchedEffect(isEditMerchantDataSuccess) {
        if (isEditMerchantDataSuccess) {
            merchantDataViewModel.editMerchantDataSuccess()
        }

    }*/

    Scaffold(
        topBar = {
            MerchantDataTopBar(
                selectCount = selectedList.size,
                name = merchant?.name ?: "",
                description = merchant?.details ?: "",
                onClick = { merchant?.let { onProfileClick(it.id) } },
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MyAppTheme.colors.transparent)
                ) {
                    CircularProgressIndicator()
                }
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
                    items(count = lazyPagingData.itemCount,
                        key = lazyPagingData.itemKey { item -> item.id }
                    ) { index ->

                        val baseColor = MyAppTheme.colors.itemBg
                        val targetAnimColor = MyAppTheme.colors.lightBlue1

                        val itemAnimateColor = remember {
                            Animatable(baseColor)
                        }

                        val data = lazyPagingData[index]
                        if (data != null) {

                            val modifierAdd: Modifier =
                                if (editMerchantId == data.id && editDataAnimRun) {
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

                            val currentDate = Util.getDateFromMillis(data.dateInMilli)
                            val previousDate =
                                if (index != lazyPagingData.itemCount - 1) lazyPagingData[index + 1]?.let {
                                    Util.getDateFromMillis(
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
                totalIncome = merchant?.incomeAmount ?: 0.0,
                totalExpense = merchant?.expenseAmount ?: 0.0,
                isEditable = isEditable,
                isDeletable = isDeletable,
                onEditClick = { merchantDataViewModel.onEditClick { onEditClick(it) } },
                onDeleteClick = { openAlertDialog = true }
            )

        }
        val context = LocalContext.current
        val merchantDataDeleteToast =
            stringResource(id = R.string.merchant_data_delete_success_message)

        if (openAlertDialog) {
            DeleteAlertDialog(
                dialogTitle = R.string.delete_dialog_title,
                dialogText = R.string.delete_item_dialog_text,
                onConfirmation = {
                    merchantDataViewModel.onDeleteDialogClick {
                        openAlertDialog = false
                        Toast.makeText(context, merchantDataDeleteToast, Toast.LENGTH_SHORT).show()
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
            onEditClick = {}
        )
    }
}