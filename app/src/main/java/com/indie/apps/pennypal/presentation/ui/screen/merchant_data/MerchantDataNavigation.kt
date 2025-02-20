package com.indie.apps.pennypal.presentation.ui.screen.merchant_data

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToMerchantDataScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.MERCHANT_DATA.route) { backStackEntry ->

        val merchantDataId: Long? =
            backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)

        val isEditMerchantDataSuccess: Boolean? =
            backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS)

        val isAddMerchantDataSuccess: Boolean? =
            backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)

       LaunchedEffect(merchantDataId) {
           backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)
           backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)
           backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS)
           backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)
       }

        bottomBarState.value = false
        MerchantDataScreen(
            onProfileClick = {
                navController.navigate(
                    ScreenNav.MERCHANT_PROFILE.route.replace(
                        "{${Util.PARAM_MERCHANT_ID}}", it.toString()
                    )
                )
            },
            onNavigationUp = { navController.navigateUp() },
            onEditClick = {
                navController.navigate(
                    ScreenNav.ADD_EDIT_MERCHANT_DATA.route.replace(
                        "{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}", it.toString()
                    )
                )
            },
            onAddClick = {
                navController.navigate(ScreenNav.ADD_EDIT_MERCHANT_DATA.route)

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_MERCHANT_NAME_DESC,
                    Gson().toJson(it)
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_MERCHANT_LOCK,
                    true
                )

            },
            isEditSuccess = isEditMerchantDataSuccess ?: false,
            isAddSuccess = isAddMerchantDataSuccess ?: false,
            merchantDataId = merchantDataId ?: -1
        )
    }
}