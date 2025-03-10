package com.indie.apps.pennypal.presentation.ui.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.screen.category.navigateToCategoryScreen

fun NavGraphBuilder.categoryRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    navigation(
        startDestination = ScreenNav.CATEGORY_START.route, route = BottomNavItem.CATEGORY.route
    ) {
        navigateToCategoryScreen(navController, bottomBarState, innerPadding)
        /*navigateToMerchantScreen(navController, bottomBarState, innerPadding)
        navigateToMerchantDataScreen(navController, bottomBarState)
        navigateToMerchantProfileScreen(navController, bottomBarState)
        navigateToEditItemScreen(navController, bottomBarState)
*/
    }
}