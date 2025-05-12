package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.common.AccountTypeItem
import com.indie.apps.pennypal.presentation.ui.component.composable.common.TopBarWithTitle
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.ConfirmationDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.PrimaryButton
import com.indie.apps.pennypal.presentation.ui.component.extension.modifier.backgroundGradientsBrush
import com.indie.apps.pennypal.presentation.ui.component.extension.showToast
import com.indie.apps.pennypal.presentation.ui.shared_viewmodel.ads.AdViewModel
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.app_enum.DialogType

@Composable
fun PaymentScreen(
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    adViewModel: AdViewModel = hiltViewModel(),
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
    var openDialog by remember { mutableStateOf<DialogType?>(null) }
    var deletePaymentId by remember { mutableLongStateOf(0) }
    val currentAnim by paymentViewModel.currentAnim.collectAsStateWithLifecycle()
    val paymentAnimId by paymentViewModel.paymentAnimId.collectAsStateWithLifecycle()

    BackHandler {
        onNavigationUp()
    }

    LaunchedEffect(paymentId) {
        if (isEditSuccess) {
            paymentViewModel.editPaymentSuccess(paymentId)
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
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

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
                            openDialog = DialogType.Delete
                        },
                        paymentAnimId = paymentAnimId,
                        currentAnim = currentAnim,
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
                            openDialog = DialogType.Delete
                        },
                        paymentAnimId = paymentAnimId,
                        currentAnim = currentAnim,
                    )
                }
            }
        }

        openDialog?.let { dialog ->
            when (dialog) {
                DialogType.Delete -> {
                    ConfirmationDialog(
                        dialogTitle = R.string.delete_dialog_title,
                        dialogText = R.string.delete_payment_dialog_text,
                        onConfirmation = {
                            paymentViewModel.onDeleteDialogClick(deletePaymentId) {
                                openDialog = null
                                deletePaymentId = 0
                                context.showToast(paymentDeleteToast)
                            }
                        },
                        onDismissRequest = {
                            openDialog = null
                            deletePaymentId = 0
                        }
                    )
                }

                else -> {}
            }
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
