package com.indie.apps.pennypal.presentation.ui.screen.budget

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToBudgetScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.BUDGET.route) {
        bottomBarState.value = false
        BudgetScreen(
            onNavigationUp = { navController.navigateUp() },
            onAddClick = {
                navController.navigate(ScreenNav.ADD_BUDGET.route)

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_PERIOD_TYPE,
                    it
                )
            },
            onBudgetEditClick = {
                navController.navigate(
                    ScreenNav.SINGLE_BUDGET_ANALYSIS.route.replace(
                        "{${Util.PARAM_BUDGET_ID}}", it.toString()
                    )
                )

                /* navController.navigate(
                     ScreenNav.EDIT_BUDGET.route.replace(
                         "{${Util.PARAM_BUDGET_ID}}", it.toString()
                     )
                 )*/
            },
            onBudgetMenuClick = {
                navController.navigate(ScreenNav.BUDGET_FILTER.route)

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_BUDGET_FILTER_ID,
                    it
                )
            },
        )
    }
}