package com.indie.apps.pannypal.presentation.ui.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.MerchantNav

fun NavGraphBuilder.MerchantRoute(navController: NavHostController) {
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