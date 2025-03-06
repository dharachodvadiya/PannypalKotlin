package com.indie.apps.pennypal.presentation.ui.screen.merchant

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToMerchantScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    composable(route = ScreenNav.MERCHANT_START.route) { backStackEntry ->

        val savedStateHandle = backStackEntry.savedStateHandle
        // get data passed back from B
        val isAddSuccess = savedStateHandle.get<Boolean>(Util.SAVE_STATE_ADD_MERCHANT_SUCCESS)

        val isEditSuccess = savedStateHandle.get<Boolean>(Util.SAVE_STATE_EDIT_MERCHANT_SUCCESS)

        val editAddId = savedStateHandle.get<Long>(Util.SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID)

        /* val isAddMerchantDataSuccess =
             savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)

         val merchantId = savedStateHandle.get<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)*/

        LaunchedEffect(editAddId) {
            savedStateHandle.remove<Boolean>(Util.SAVE_STATE_EDIT_MERCHANT_SUCCESS)
            savedStateHandle.remove<Boolean>(Util.SAVE_STATE_ADD_MERCHANT_SUCCESS)
            savedStateHandle.remove<Long>(Util.SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID)

            savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)
            savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)
            savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)
        }

        bottomBarState.value = true

        MerchantScreen(
            isAddSuccess = isAddSuccess ?: false,
            isEditSuccess = isEditSuccess ?: false,
            editAddId = editAddId ?: -1,
            onMerchantClick = {
                navController.navigate(
                    ScreenNav.MERCHANT_DATA.route.replace(
                        "{${Util.PARAM_MERCHANT_ID}}", it.toString()
                    )
                )
            },
            onAddClick = { navController.navigate(DialogNav.ADD_EDIT_MERCHANT.route) },
            onEditClick = {

                navController.navigate(
                    DialogNav.ADD_EDIT_MERCHANT.route.replace(
                        "{${Util.PARAM_MERCHANT_ID}}", it.toString()
                    )
                )
            },
            bottomPadding = innerPadding,
            isAddMerchantDataSuccess = false,
            merchantId = -1,
            onNavigationUp = {
                //navController.navigateUp()
                navController.navigate(BottomNavItem.OVERVIEW.route) {
                    launchSingleTop = true
                }
            }
        )


    }
}