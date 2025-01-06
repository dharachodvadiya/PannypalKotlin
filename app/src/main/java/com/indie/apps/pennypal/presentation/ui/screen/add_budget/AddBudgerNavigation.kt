package com.indie.apps.pennypal.presentation.ui.screen.add_budget

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToAddBudgetScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.ADD_BUDGET.route) { backStackEntry ->

        val gsonStringCategoryIds =
            backStackEntry.savedStateHandle.get<String>(Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST)

        val categoryIds: List<Long> = gsonStringCategoryIds?.let {
            Gson().fromJson(it, object : TypeToken<List<Long>>() {}.type)
        } ?: emptyList()

        val periodType =
            backStackEntry.savedStateHandle.get<Int>(Util.SAVE_STATE_PERIOD_TYPE)

        bottomBarState.value = false
        AddBudgetScreen(
            onNavigationUp = { navController.navigateUp() },
            onSave = { _, _ ->
                navController.popBackStack()
            },
            onSelectCategory = { selectedIds ->
                backStackEntry
                    .savedStateHandle[Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST] =
                    Gson().toJson(selectedIds)
                navController.navigate(DialogNav.MULTI_SELECT_CATEGORY.route)
            },
            selectedCategoryIds = categoryIds,
            selectedPeriodType = periodType ?: 1
        )
    }
}

internal fun NavGraphBuilder.navigateToEditBudgetScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.EDIT_BUDGET.route) { backStackEntry ->

        val gsonStringCategoryIds =
            backStackEntry.savedStateHandle.get<String>(Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST)

        val categoryIds: List<Long> = gsonStringCategoryIds?.let {
            Gson().fromJson(it, object : TypeToken<List<Long>>() {}.type)
        } ?: emptyList()

        bottomBarState.value = false
        AddBudgetScreen(
            onNavigationUp = { navController.navigateUp() },
            onSave = { _, _ ->
                navController.popBackStack()
            },
            onSelectCategory = { selectedIds ->
                backStackEntry
                    .savedStateHandle[Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST] =
                    Gson().toJson(selectedIds)
                navController.navigate(DialogNav.MULTI_SELECT_CATEGORY.route)
            },
            selectedCategoryIds = categoryIds,
            selectedPeriodType = 1
        )
    }
}