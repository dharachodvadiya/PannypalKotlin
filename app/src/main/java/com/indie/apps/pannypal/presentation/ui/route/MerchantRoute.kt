package com.indie.apps.pannypal.presentation.ui.route

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.MerchantNav

fun NavGraphBuilder.MerchantRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    navigation(
        startDestination = MerchantNav.START.route,
        route = BottomNavItem.MERCHANTS.route
    ) {
        composable(route = MerchantNav.START.route) {

        }
        composable(route = MerchantNav.DATA.route) {

        }
    }
}