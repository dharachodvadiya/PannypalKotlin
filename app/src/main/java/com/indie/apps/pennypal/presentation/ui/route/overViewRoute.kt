package com.indie.apps.pennypal.presentation.ui.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.screen.add_budget.navigateToAddEditBudgetScreen
import com.indie.apps.pennypal.presentation.ui.screen.all_data.navigateToAllDataScreen
import com.indie.apps.pennypal.presentation.ui.screen.budget.navigateToBudgetScreen
import com.indie.apps.pennypal.presentation.ui.screen.budget_filter.navigateToBudgetFilterScreen
import com.indie.apps.pennypal.presentation.ui.screen.new_item.navigateToEditItemScreen
import com.indie.apps.pennypal.presentation.ui.screen.overview.navigateToOverViewStartScreen
import com.indie.apps.pennypal.presentation.ui.screen.overview_analysis.navigateToOverViewAnalysisScreen
import com.indie.apps.pennypal.presentation.ui.screen.profile.navigateToProfileScreen
import com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis.navigateToSingleBudgetScreen


fun NavGraphBuilder.overViewRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {

    navigation(
        startDestination = ScreenNav.OVERVIEW_START.route, route = BottomNavItem.OVERVIEW.route
    ) {
        navigateToOverViewStartScreen(navController, bottomBarState, innerPadding)

        navigateToEditItemScreen(navController, bottomBarState)

        navigateToProfileScreen(navController, bottomBarState)

        navigateToAllDataScreen(navController, bottomBarState)

        navigateToOverViewAnalysisScreen(navController, bottomBarState)

        navigateToBudgetScreen(navController, bottomBarState)

        navigateToSingleBudgetScreen(navController, bottomBarState)

        //navigateToAddBudgetScreen(navController, bottomBarState)

        navigateToAddEditBudgetScreen(navController, bottomBarState)

        navigateToBudgetFilterScreen(navController, bottomBarState)


    }
}