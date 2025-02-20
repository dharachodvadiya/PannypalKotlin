package com.indie.apps.pennypal.presentation.ui.screen.overview

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

internal fun NavGraphBuilder.navigateToOverViewStartScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    composable(route = ScreenNav.OVERVIEW_START.route) { backStackEntry ->

        val merchantDataId: Long? =
            backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)

        val merchantId: Long? =
            backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID)

        val isAddMerchantDataSuccess: Boolean? =
            backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)

        val isEditMerchantDataSuccess: Boolean? =
            backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS)

        bottomBarState.value = true

        LaunchedEffect(merchantDataId) {
            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)
            backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)
            backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS)
        }

        LaunchedEffect(merchantId) {
            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)
            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID)
        }


        OverViewStartScreen(
            addEditMerchantDataId = merchantDataId ?: -1,
            isAddMerchantDataSuccess = isAddMerchantDataSuccess ?: false,
            isEditSuccess = isEditMerchantDataSuccess ?: false,
            onSeeAllTransactionClick = {
                navController.navigate(ScreenNav.SEE_ALL_DATA.route)
            },
            onSeeAllMerchantClick = {
                navController.navigate(BottomNavItem.MERCHANTS.route)
            },
            onExploreAnalysisClick = {
                navController.navigate(ScreenNav.OVERVIEW_ANALYSIS.route)
            },
            onTransactionClick = {
                navController.navigate(
                    ScreenNav.ADD_EDIT_MERCHANT_DATA.route.replace(
                        "{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}", it.toString()
                    )
                )
            },
            onExploreBudgetClick = {
                navController.navigate(ScreenNav.BUDGET.route)
            },
            onAddMerchant = {
                navController.navigate(DialogNav.ADD_EDIT_MERCHANT.route)
            },
            addMerchantId = merchantId ?: -1L,
            onSetBudgetClick = {
                navController.navigate(ScreenNav.ADD_EDIT_BUDGET.route)

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_PERIOD_TYPE,
                    it
                )
            },
            onMerchantClick = {
                navController.navigate(
                    ScreenNav.MERCHANT_DATA.route.replace(
                        "{${Util.PARAM_MERCHANT_ID}}", it.toString()
                    )
                )
            },
            bottomPadding = innerPadding
        )
    }
}