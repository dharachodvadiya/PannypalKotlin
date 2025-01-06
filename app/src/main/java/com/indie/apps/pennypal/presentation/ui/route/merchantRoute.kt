package com.indie.apps.pennypal.presentation.ui.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.screen.merchant.navigateToMerchantScreen
import com.indie.apps.pennypal.presentation.ui.screen.merchant_data.navigateToMerchantDataScreen
import com.indie.apps.pennypal.presentation.ui.screen.merchant_profile.navigateToMerchantProfileScreen
import com.indie.apps.pennypal.presentation.ui.screen.new_item.navigateToEditItemScreen

fun NavGraphBuilder.merchantRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    navigation(
        startDestination = ScreenNav.MERCHANT_START.route, route = BottomNavItem.MERCHANTS.route
    ) {
        navigateToMerchantScreen(navController, bottomBarState, innerPadding)
        navigateToMerchantDataScreen(navController, bottomBarState)
        navigateToMerchantProfileScreen(navController, bottomBarState)
        navigateToEditItemScreen(navController, bottomBarState)

    }
}