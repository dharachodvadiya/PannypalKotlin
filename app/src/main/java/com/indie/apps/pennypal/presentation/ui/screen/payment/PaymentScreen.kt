package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.AccountTypeItem
import com.indie.apps.pennypal.presentation.ui.component.BottomSaveButton
import com.indie.apps.pennypal.presentation.ui.component.DeleteAlertDialog
import com.indie.apps.pennypal.presentation.ui.component.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.custom.composable.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme

@Composable
fun PaymentScreen(
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    isEditSuccess: Boolean = false,
    onEditPaymentClick: (Long) -> Unit,
    onAddPaymentClick: () -> Unit,
    paymentId: Long = 0L,
    onModeChange: (Boolean) -> Unit
) {

    val isEditMode by paymentViewModel.isInEditMode.collectAsStateWithLifecycle()

    val paymentWithModeList by paymentViewModel.paymentWithModeState.collectAsStateWithLifecycle()
    val userData by paymentViewModel.userState.collectAsStateWithLifecycle()

    var defaultPaymentId by remember {
        mutableLongStateOf(1L)
    }

    LaunchedEffect(isEditMode) {
        if (isEditMode)
            defaultPaymentId = userData?.paymentId ?: 1L
    }

    LaunchedEffect(Unit) {
        onModeChange(false)
    }
    val context = LocalContext.current
    val paymentDeleteToast = stringResource(id = R.string.payment_delete_success_message)
    var openDeleteDialog by remember { mutableStateOf(false) }
    var deletePaymentId by remember { mutableLongStateOf(0) }

    val editAnimRun by paymentViewModel.editAnimRun.collectAsStateWithLifecycle()

    var editedPaymentId by remember {
        mutableLongStateOf(-1L)
    }
    var isEditPaymentSuccessState by remember {
        mutableStateOf(false)
    }

    if (isEditPaymentSuccessState != isEditSuccess) {
        if (isEditSuccess) {
            editedPaymentId = paymentId
            paymentViewModel.editPaymentSuccess()
        }
        isEditPaymentSuccessState = isEditSuccess
    }

    Scaffold(
        topBar = {
            TopBarWithTitle(
                isBackEnable = isEditMode,
                onNavigationUp = {
                    paymentViewModel.setEditMode(false)
                    onModeChange(false)
                },
                title = if (isEditMode) stringResource(id = R.string.edit_account_details) else stringResource(
                    id = R.string.accounts
                ),
                contentAlignment = Alignment.Center,
                bgColor = MyAppTheme.colors.transparent,
                trailingContent = {
                    if (isEditMode) {
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
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "edit",
                            tint = MyAppTheme.colors.black,
                            modifier = Modifier
                                .roundedCornerBackground(MyAppTheme.colors.transparent)
                                .size(25.dp)
                                .clickable {
                                    paymentViewModel.setEditMode(true)
                                    onModeChange(true)
                                }
                        )
                    }

                }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradientsBrush(MyAppTheme.colors.gradientBg))
                .padding(innerPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding))
        ) {
            val paymentName = if (paymentWithModeList.isNotEmpty()) {
                paymentWithModeList.first { item ->
                    item.id == (userData?.paymentId ?: 1L)
                }.name
            } else {
                "Cash"
            }

            if (!isEditMode) {
                PaymentModeDefaultItem(paymentName)
            }

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
                        isEditMode = isEditMode,
                        isEditable = isEditMode,
                        dataList = bankList,
                        selectPaymentId = defaultPaymentId,
                        onSelect = {
                            defaultPaymentId = it.id
                        },
                        onEditClick = onEditPaymentClick,
                        editAnimPaymentId = editedPaymentId,
                        editAnimRun = editAnimRun,
                        onDeleteClick = {
                            deletePaymentId = it.id
                            openDeleteDialog = true
                        }
                    )
                }

                if (cashList.isNotEmpty()) {
                    AccountTypeItem(
                        titleId = R.string.cash,
                        isEditMode = isEditMode,
                        isEditable = isEditMode,
                        dataList = cashList,
                        selectPaymentId = defaultPaymentId,
                        onSelect = {
                            defaultPaymentId = it.id
                        },
                        onEditClick = {},
                        editAnimPaymentId = editedPaymentId,
                        editAnimRun = editAnimRun,
                        onDeleteClick = {
                            deletePaymentId = it.id
                            openDeleteDialog = true
                            //onDeletePaymentClick(it.toPaymentWithIdName())
                        }
                    )
                }
            }

            if (isEditMode) {
                Spacer(modifier = Modifier.weight(1f))
                BottomSaveButton(
                    onClick = {
                        paymentViewModel.saveEditedData(defaultPaymentId) {
                            onModeChange(false)
                        }
                    },
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding))
                )
            }
        }

        if (openDeleteDialog) {
            DeleteAlertDialog(
                dialogTitle = R.string.delete_dialog_title,
                dialogText = R.string.delete_payment_dialog_text,
                onConfirmation = {
                    paymentViewModel.onDeleteDialogClick(deletePaymentId) {
                        if (deletePaymentId == defaultPaymentId) {
                            defaultPaymentId = 1L
                        }
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
            onModeChange = {},
            onEditPaymentClick = {},
            onAddPaymentClick = {}
        )
    }
}
