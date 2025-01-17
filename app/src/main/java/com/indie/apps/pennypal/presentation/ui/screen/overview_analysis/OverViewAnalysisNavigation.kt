package com.indie.apps.pennypal.presentation.ui.screen.overview_analysis

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav

internal fun NavGraphBuilder.navigateToOverViewAnalysisScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.OVERVIEW_ANALYSIS.route) {
        bottomBarState.value = false
        OverViewAnalysisScreen(
            onNavigationUp = { navController.navigateUp() }
        )
    }
}