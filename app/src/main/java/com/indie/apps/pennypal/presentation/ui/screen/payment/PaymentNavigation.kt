package com.indie.apps.pennypal.presentation.ui.screen.payment

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToPaymentScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    composable(route = ScreenNav.PAYMENT_START.route) { backStackEntry ->
        bottomBarState.value = true

        val savedStateHandle = backStackEntry.savedStateHandle

        val isEditSuccess = savedStateHandle.get<Boolean>(Util.SAVE_STATE_PAYMENT_EDIT_SUCCESS)
        val editAddId = savedStateHandle.get<Long>(Util.SAVE_STATE_PAYMENT_ADD_EDIT_ID)

        savedStateHandle.remove<Boolean>(Util.SAVE_STATE_PAYMENT_EDIT_SUCCESS)
        savedStateHandle.remove<Boolean>(Util.SAVE_STATE_PAYMENT_ADD_EDIT_ID)

        PaymentScreen(
            isEditSuccess = isEditSuccess ?: false,
            paymentId = editAddId ?: -1L,
            onDefaultPaymentChange = { currentId ->
                navController.navigate(DialogNav.SELECT_PAYMENT.route)
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SELECT_PAYMENT_ID, currentId
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SAVABLE_DIALOG, true
                )
            },
            onEditPaymentClick = {

                navController.navigate(
                    DialogNav.ADD_EDIT_PAYMENT.route.replace(
                        "{${Util.PARAM_PAYMENT_ID}}", it.toString()
                    )
                )
            },
            onAddPaymentClick = {
                navController.navigate(DialogNav.ADD_EDIT_PAYMENT.route)
            },
            onNavigationUp = {
                navController.popBackStack()
            },
            bottomPadding = innerPadding
        )
    }
}