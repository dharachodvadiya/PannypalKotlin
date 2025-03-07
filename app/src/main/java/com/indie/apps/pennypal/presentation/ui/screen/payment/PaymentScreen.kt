package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.AccountTypeItem
import com.indie.apps.pennypal.presentation.ui.component.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun PaymentScreen(
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    isEditSuccess: Boolean = false,
    onEditPaymentClick: (Long) -> Unit,
    onAddPaymentClick: () -> Unit,
    onDefaultPaymentChange: (Long) -> Unit,
    onNavigationUp: () -> Unit,
    paymentId: Long = 0L,
    bottomPadding: PaddingValues,
) {

    val paymentWithModeList by paymentViewModel.paymentWithModeState.collectAsStateWithLifecycle()
    val userData by paymentViewModel.userState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val paymentDeleteToast = stringResource(id = R.string.payment_delete_success_message)
    var openDeleteDialog by remember { mutableStateOf(false) }
    var deletePaymentId by remember { mutableLongStateOf(0) }
    val editAnimRun by paymentViewModel.editAnimRun.collectAsStateWithLifecycle()

    var editedPaymentId by remember {
        mutableLongStateOf(-1L)
    }

    BackHandler {
        onNavigationUp()
    }

    LaunchedEffect(paymentId) {
        if (isEditSuccess) {
            editedPaymentId = paymentId
            paymentViewModel.editPaymentSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopBarWithTitle(
                onNavigationUp = {
                    onNavigationUp()

                },
                title = stringResource(id = R.string.accounts),
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.transparent,
                trailingContent = {
                    PrimaryButton(
                        bgColor = MyAppTheme.colors.white,
                        borderStroke = BorderStroke(
                            width = 1.dp,
                            color = MyAppTheme.colors.gray1
                        ),
                        onClick = onAddPaymentClick,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.top_bar_profile))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = MyAppTheme.colors.gray1
                        )
                    }
                }
            )
        }
    ) { topBarPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(horizontal = dimensionResource(id = R.dimen.padding))
                .padding(
                    top = topBarPadding.calculateTopPadding(),
                    bottom = bottomPadding.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
        ) {
            val paymentName = if (paymentWithModeList.isNotEmpty()) {
                paymentWithModeList.firstOrNull() { item ->
                    item.id == (userData?.paymentId ?: -1L)
                }?.name ?: "Cash"
            } else {
                "Cash"
            }

            PaymentModeDefaultItem(
                paymentModeName = paymentName,
                onDefaultPaymentChange = {
                    onDefaultPaymentChange(userData?.paymentId ?: -1L)
                }
            )

            val bankList = paymentWithModeList.filter { item ->
                item.modeName != "Cash"
            }

            val cashList = paymentWithModeList.filter { item ->
                item.modeName == "Cash"
            }

            if (userData != null) {
                if (bankList.isNotEmpty()) {

                    AccountTypeItem(
                        titleId = R.string.bank,
                        isSelectable = false,
                        isEditable = true,
                        dataList = bankList,
                        selectPaymentId = userData?.paymentId ?: -1L,
                        onEditClick = {
                            onEditPaymentClick(it)
                        },
                        onDeleteClick = {
                            deletePaymentId = it.id
                            openDeleteDialog = true
                        },
                        editAnimPaymentId = editedPaymentId,
                        editAnimRun = editAnimRun,
                    )
                }

                if (cashList.isNotEmpty()) {
                    AccountTypeItem(
                        titleId = R.string.bank,
                        isSelectable = false,
                        isEditable = true,
                        dataList = cashList,
                        selectPaymentId = userData?.paymentId ?: -1L,
                        onEditClick = {
                            onEditPaymentClick(it)
                        },
                        onDeleteClick = {
                            deletePaymentId = it.id
                            openDeleteDialog = true
                        },
                        editAnimPaymentId = editedPaymentId,
                        editAnimRun = editAnimRun,
                    )
                }
            }
        }

        if (openDeleteDialog) {
            ConfirmationDialog(
                dialogTitle = R.string.delete_dialog_title,
                dialogText = R.string.delete_payment_dialog_text,
                onConfirmation = {
                    paymentViewModel.onDeleteDialogClick(deletePaymentId) {
                        openDeleteDialog = false
                        deletePaymentId = 0
                        context.showToast(paymentDeleteToast)
                    }
                },
                onDismissRequest = {
                    openDeleteDialog = false
                    deletePaymentId = 0
                }
            )
        }
    }
}

@Preview
@Composable
private fun PaymentScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        PaymentScreen(
            onEditPaymentClick = {},
            onAddPaymentClick = {},
            onNavigationUp = {},
            onDefaultPaymentChange = {},
            bottomPadding = PaddingValues(0.dp)
        )
    }
}
