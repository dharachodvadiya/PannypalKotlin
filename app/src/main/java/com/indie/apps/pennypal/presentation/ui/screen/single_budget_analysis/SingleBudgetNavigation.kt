package com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToSingleBudgetScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    composable(route = ScreenNav.SINGLE_BUDGET_ANALYSIS.route) {
        bottomBarState.value = false
        SingleBudgetScreen(
            onNavigationUp = { navController.navigateUp() },
            onEditClick = {
                navController.navigate(
                    ScreenNav.ADD_EDIT_BUDGET.route.replace(
                        "{${Util.PARAM_BUDGET_ID}}", it.toString()
                    )
                )
            },
            onDeleteSuccess = { id ->

                navController.navigateUp()
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_BUDGET_ID,
                    id
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_BUDGET_DELETE_SUCCESS,
                    true
                )

            }
        )
    }
}