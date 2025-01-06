package com.indie.apps.pennypal.presentation.ui.screen.merchant_profile

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav

internal fun NavGraphBuilder.navigateToMerchantProfileScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.MERCHANT_PROFILE.route) {
        bottomBarState.value = false
        MerchantProfileScreen(onNavigationUp = { navController.navigateUp() })
    }
}