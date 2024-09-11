package com.indie.apps.pennypal.presentation.ui.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.screen.payment.PaymentScreen
import com.indie.apps.pennypal.util.Util

fun NavGraphBuilder.paymentRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    navigation(
        startDestination = ScreenNav.PAYMENT_START.route,
        route = BottomNavItem.ACCOUNTS.route
    ) {
        composable(route = ScreenNav.PAYMENT_START.route) { backStackEntry ->
            //bottomBarState.value = true

            val savedStateHandle = backStackEntry.savedStateHandle
            
            val isEditSuccess = savedStateHandle.get<Boolean>(Util.SAVE_STATE_PAYMENT_EDIT_SUCCESS)
            val editAddId = savedStateHandle.get<Long>(Util.SAVE_STATE_PAYMENT_ADD_EDIT_ID)

            savedStateHandle.remove<Boolean>(Util.SAVE_STATE_PAYMENT_EDIT_SUCCESS)
            savedStateHandle.remove<Boolean>(Util.SAVE_STATE_PAYMENT_ADD_EDIT_ID)

            PaymentScreen(
                isEditSuccess = isEditSuccess ?: false,
                paymentId = editAddId ?: -1L,
                onEditPaymentClick = {
                    navController.navigate(DialogNav.ADD_EDIT_PAYMENT.route)

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(Util.SAVE_STATE_PAYMENT_EDIT_ID, it)
                },
                onModeChange = { isEditMode ->
                    bottomBarState.value = !isEditMode
                },
                onAddPaymentClick = {
                    navController.navigate(DialogNav.ADD_EDIT_PAYMENT.route)
                }
            )
        }
    }
}