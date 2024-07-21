package com.indie.apps.pannypal.presentation.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.OverviewNav
import com.indie.apps.pannypal.presentation.ui.screen.OverViewStartScreen


fun NavGraphBuilder.OverViewRoute(navController: NavHostController) {
    navigation(
        startDestination = OverviewNav.START.route, route = BottomNavItem.OVERVIEW.route
    ) {
        composable(route = OverviewNav.START.route) {
            OverViewStartScreen(onNewEntry = { navController.navigate(OverviewNav.NEW_ITEM.route) },
                onProfileClick = { navController.navigate(OverviewNav.PROFILE.route) })
        }
        composable(route = OverviewNav.NEW_ITEM.route) {

        }
        composable(route = OverviewNav.PROFILE.route) {

        }
    }
}