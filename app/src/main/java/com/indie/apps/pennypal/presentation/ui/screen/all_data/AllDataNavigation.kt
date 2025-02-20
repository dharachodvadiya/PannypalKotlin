package com.indie.apps.pennypal.presentation.ui.screen.all_data

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToAllDataScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.SEE_ALL_DATA.route) { backStackEntry ->

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
        AllDataScreen(
            onDataClick = {
                navController.navigate(
                    ScreenNav.ADD_EDIT_MERCHANT_DATA.route.replace(
                        "{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}", it.toString()
                    )
                )
            },
            onAddClick = {
                navController.navigate(ScreenNav.ADD_EDIT_MERCHANT_DATA.route)
            },
            onNavigationUp = {
                navController.navigateUp()
            },
            editAddId = merchantDataId ?: -1,
            isEditSuccess = isEditMerchantDataSuccess ?: false,
            isAddSuccess = isAddMerchantDataSuccess ?: false,
        )


    }
}