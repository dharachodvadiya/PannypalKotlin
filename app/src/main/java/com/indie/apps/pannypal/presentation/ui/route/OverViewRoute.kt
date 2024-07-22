package com.indie.apps.pannypal.presentation.ui.route

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.OverviewNav
import com.indie.apps.pannypal.presentation.ui.screen.NewItemScreen
import com.indie.apps.pannypal.presentation.ui.screen.OverViewStartScreen
import com.indie.apps.pannypal.presentation.ui.screen.ProfileScreen


fun NavGraphBuilder.OverViewRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    navigation(
        startDestination = OverviewNav.START.route, route = BottomNavItem.OVERVIEW.route
    ) {
        composable(route = OverviewNav.START.route) {
            bottomBarState.value = true
            OverViewStartScreen(onNewEntry = { navController.navigate(OverviewNav.NEW_ITEM.route) },
                onProfileClick = { navController.navigate(OverviewNav.PROFILE.route) })
        }
        composable(route = OverviewNav.NEW_ITEM.route) {
            bottomBarState.value = false
            NewItemScreen(
                onNavigationUp = { navController.navigateUp() },
                onMerchantSelect = {},
                onPaymentAdd = {})
        }
        composable(route = OverviewNav.PROFILE.route) {
            bottomBarState.value = false
            ProfileScreen(onNavigationUp = { navController.navigateUp() })
        }
    }
}