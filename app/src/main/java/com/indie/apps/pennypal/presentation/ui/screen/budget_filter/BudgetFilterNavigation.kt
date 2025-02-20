package com.indie.apps.pennypal.presentation.ui.screen.budget_filter

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToBudgetFilterScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.BUDGET_FILTER.route) { backStackEntry ->
        bottomBarState.value = false

        val menuId =
            backStackEntry.savedStateHandle.get<Int>(Util.SAVE_STATE_BUDGET_FILTER_ID)

        val periodType =
            backStackEntry.savedStateHandle.get<Int>(Util.SAVE_STATE_PERIOD_TYPE)

        val budgetId: Long? =
            backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_BUDGET_ID)

        LaunchedEffect(periodType) {
            backStackEntry.savedStateHandle.remove<Int>(Util.SAVE_STATE_PERIOD_TYPE)
        }
        LaunchedEffect(budgetId) {
            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_BUDGET_ID)
        }

        BudgetFilterScreen(
            onNavigationUp = { navController.navigateUp() },
            onAddClick = {
                navController.navigate(ScreenNav.ADD_EDIT_BUDGET.route)

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
            },
            budgetFilterId = menuId ?: 1,
            periodType = periodType,
            budgetId = budgetId
        )


    }
}