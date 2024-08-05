package com.indie.apps.pannypal.presentation.ui.route

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pannypal.presentation.ui.navigation.MerchantNav
import com.indie.apps.pannypal.presentation.ui.screen.MerchantDataScreen
import com.indie.apps.pannypal.presentation.ui.screen.MerchantProfileScreen
import com.indie.apps.pannypal.presentation.ui.screen.MerchantScreen

fun NavGraphBuilder.MerchantRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    navigation(
        startDestination = MerchantNav.START.route,
        route = BottomNavItem.MERCHANTS.route
    ) {
        composable(route = MerchantNav.START.route) {backStackEntry ->
            // get data passed back from B
            val isEditAddSuccess: Boolean? = backStackEntry
                .savedStateHandle
                .get<Boolean>(Util.SAVE_STATE_ADD_EDIT_SUCCESS)

            backStackEntry
                .savedStateHandle
                .remove<Boolean>(Util.SAVE_STATE_ADD_EDIT_SUCCESS)

            bottomBarState.value = true
            MerchantScreen(
                isEditAddSuccess = isEditAddSuccess?: false,
                onMerchantClick = {
                    navController.navigate(MerchantNav.DATA.route.replace("{${Util.PARAM_MERCHANT_ID}}", it.toString())) },
                onAddClick = { navController.navigate(DialogNav.ADD_MERCHANT.route) },
                onEditClick = {

                    navController.navigate(DialogNav.ADD_MERCHANT.route)

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(Util.SAVE_STATE_EDIT_ID, it)
                }
            )
        }
        composable(route = MerchantNav.DATA.route) {
            bottomBarState.value = false
            MerchantDataScreen(
                onProfileClick = { navController.navigate(MerchantNav.PROFILE.route) },
                onNavigationUp = { navController.navigateUp() })
        }

        composable(route = MerchantNav.PROFILE.route) {
            bottomBarState.value = false
            MerchantProfileScreen(
                onNavigationUp = { navController.navigateUp() })
        }
    }
}