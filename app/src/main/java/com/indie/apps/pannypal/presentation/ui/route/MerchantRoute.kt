package com.indie.apps.pannypal.presentation.ui.route

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pannypal.presentation.ui.navigation.MerchantNav
import com.indie.apps.pannypal.presentation.ui.screen.MerchantScreen

fun NavGraphBuilder.MerchantRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    navigation(
        startDestination = MerchantNav.START.route,
        route = BottomNavItem.MERCHANTS.route
    ) {
        composable(route = MerchantNav.START.route) {
            bottomBarState.value = true
            MerchantScreen(
                onAddClick = { navController.navigate(DialogNav.ADD_MERCHANT.route) },
                onEditClick = { navController.navigate(DialogNav.EDIT_MERCHANT.route) },
                onDeleteClick = {}
            )
        }
        composable(route = MerchantNav.DATA.route) {
            bottomBarState.value = false
        }
    }
}