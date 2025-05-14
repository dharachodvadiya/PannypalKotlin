package com.indie.apps.pennypal.presentation.ui.screen.purchase

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav

internal fun NavGraphBuilder.navigateToPurchaseScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    composable(route = ScreenNav.PURCHASE.route) { backStackEntry ->


        bottomBarState.value = false
        PurchaseScreen(
            onNavigationUp = {
                navController.navigateUp()
            }
        )
    }
}