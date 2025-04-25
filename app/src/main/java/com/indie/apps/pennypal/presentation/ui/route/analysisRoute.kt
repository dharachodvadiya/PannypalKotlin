package com.indie.apps.pennypal.presentation.ui.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.screen.overview_analysis.navigateToOverViewAnalysisScreen

fun NavGraphBuilder.analysisRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    navigation(
        startDestination = ScreenNav.ANALYSIS.route,
        route = BottomNavItem.ANALYSIS.route
    ) {
        navigateToOverViewAnalysisScreen(navController, bottomBarState, innerPadding)
    }
}