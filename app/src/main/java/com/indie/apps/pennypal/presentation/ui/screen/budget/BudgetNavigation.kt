package com.indie.apps.pennypal.presentation.ui.screen.budget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToBudgetScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    composable(route = ScreenNav.BUDGET.route) { backStackEntry ->

        val budgetId: Long? =
            backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_BUDGET_ID)

        val periodType =
            backStackEntry.savedStateHandle.get<Int>(Util.SAVE_STATE_PERIOD_TYPE)

        val isAddBudgetSuccess: Boolean? =
            backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_BUDGET_ADD_SUCCESS)

        val month: Int? =
            backStackEntry.savedStateHandle.get<Int>(Util.SAVE_STATE_BUDGET_MONTH)

        val year: Int? =
            backStackEntry.savedStateHandle.get<Int>(Util.SAVE_STATE_BUDGET_YEAR)

        val isDeleteBudgetSuccess: Boolean? =
            backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_BUDGET_DELETE_SUCCESS)

        LaunchedEffect(budgetId) {
            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_BUDGET_ID)
            backStackEntry.savedStateHandle.remove<Int>(Util.SAVE_STATE_PERIOD_TYPE)
            backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_BUDGET_ADD_SUCCESS)
            backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_BUDGET_DELETE_SUCCESS)
        }

        bottomBarState.value = false
        BudgetScreen(
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
            budgetId = budgetId ?: -1L,
            isAddSuccess = isAddBudgetSuccess ?: false,
            isDeleteSuccess = isDeleteBudgetSuccess ?: false,
            periodType = periodType,
            currentMonth = month ?: -1,
            currentYear = year ?: -1
        )
    }
}