package com.indie.apps.pennypal.presentation.ui.navigation.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.screen.payment.navigateToPaymentScreen

fun NavGraphBuilder.paymentRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    navigation(
        startDestination = ScreenNav.PAYMENT.route,
        route = BottomNavItem.ACCOUNTS.route
    ) {
        navigateToPaymentScreen(navController, bottomBarState, innerPadding)
    }
}