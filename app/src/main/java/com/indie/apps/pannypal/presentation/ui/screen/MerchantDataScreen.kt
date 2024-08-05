package com.indie.apps.pannypal.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.indie.apps.pannypal.data.entity.Merchant
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.component.DeleteAlertDialog
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataBottomBar
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataDateItem
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataExpenseAmount
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataIncomeAmount
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantDataTopBar
import com.indie.apps.pannypal.presentation.ui.component.screen.MerchantListItem
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import com.indie.apps.pannypal.presentation.viewmodel.MerchantDataViewModel
import com.indie.apps.pannypal.util.Resource

@Composable
fun MerchantDataScreen(
    merchantDataViewModel: MerchantDataViewModel = hiltViewModel(),
    onProfileClick: () -> Unit,
    onNavigationUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    val merchantState by merchantDataViewModel.merchantState.collectAsStateWithLifecycle()

    var merchant : Merchant? by remember {
        mutableStateOf(null)
    }

    when (merchantState) {
        is Resource.Loading -> null
        is Resource.Success -> {merchant = merchantState.data}

        is Resource.Error -> null
    }

    val lazyPagingData = merchantDataViewModel.pagedData.collectAsLazyPagingItems()
    merchantDataViewModel.pagingState.update(lazyPagingData)

    var openAlertDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MerchantDataTopBar(
                selectCount = merchantDataViewModel.selectedList.size,
                name = merchant?.name ?: "",
                description = merchant?.details ?: "",
                onClick =onProfileClick,
                onNavigationUp = onNavigationUp,
                onCloseClick = {
                    merchantDataViewModel.clearSelection()
                }
            )
        }
    ){padding->
        Column(
            modifier = modifier
                .padding(padding)
        ) {

            if (merchantDataViewModel.pagingState.isRefresh
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    CircularProgressIndicator()
                }
            }else {

                val scrollState: LazyListState = rememberLazyListState(
                    merchantDataViewModel.scrollIndex,
                    merchantDataViewModel.scrollOffset
                )

                // after each scroll, update values in ViewModel
                LaunchedEffect(key1 = scrollState.isScrollInProgress) {
                    if (!scrollState.isScrollInProgress) {
                        merchantDataViewModel.scrollIndex = scrollState.firstVisibleItemIndex
                        merchantDataViewModel.scrollOffset =
                            scrollState.firstVisibleItemScrollOffset
                    }
                }

                LazyColumn(
                    reverseLayout = true,
                    state = scrollState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = dimensionResource(id = R.dimen.padding))
                ) {
                    items(count = lazyPagingData.itemCount,
                        key = lazyPagingData.itemKey { item -> item.id }
                    ) { index ->


                        val data = lazyPagingData[index]
                        if (data != null) {
                            val currentDate = Util.getDateFromMillis(data.dateInMilli)
                            val previousDate = if(index != lazyPagingData.itemCount -1) lazyPagingData[index+1]?.let {
                                Util.getDateFromMillis(
                                    it.dateInMilli)
                            } else ""

                            if (data.type >= 0) {
                                MerchantDataIncomeAmount(
                                    isSelected = merchantDataViewModel.selectedList.contains(data.id),
                                    data = data,
                                    onClick = { merchantDataViewModel.onItemClick(data.id) },
                                    onLongClick = { merchantDataViewModel.onItemLongClick(data.id) }
                                )
                            } else {
                                MerchantDataExpenseAmount(
                                    isSelected = merchantDataViewModel.selectedList.contains(data.id),
                                    data = data,
                                    onClick = { merchantDataViewModel.onItemClick(data.id)},
                                    onLongClick = { merchantDataViewModel.onItemLongClick(data.id) }
                                )
                            }

                            if(currentDate != previousDate)
                            {
                                MerchantDataDateItem(
                                    dateMillis = data.dateInMilli
                                )
                            }
                        }

                        if (merchantDataViewModel.pagingState.isLoadMore && index == lazyPagingData.itemCount) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
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
                isEditable = merchantDataViewModel.isEditable,
                isDeletable = merchantDataViewModel.isDeletable,
                onEditClick = {},
                onDeleteClick = {openAlertDialog = true}
            )

        }
        val context = LocalContext.current
        val merchantDataDeleteToast = stringResource(id = R.string.merchant_data_delete_success_message)

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
    PannyPalTheme {
        MerchantDataScreen(
            onProfileClick = {},
            onNavigationUp = {}
        )
    }
}