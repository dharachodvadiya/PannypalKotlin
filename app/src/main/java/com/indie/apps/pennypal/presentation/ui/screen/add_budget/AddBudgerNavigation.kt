package com.indie.apps.pennypal.presentation.ui.screen.add_budget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

/*internal fun NavGraphBuilder.navigateToAddBudgetScreen(
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
}*/

internal fun NavGraphBuilder.navigateToAddEditBudgetScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    composable(route = ScreenNav.ADD_EDIT_BUDGET.route) { backStackEntry ->

        val gsonStringCategoryIds =
            backStackEntry.savedStateHandle.get<String>(Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST)

        val categoryIds: List<Long> = gsonStringCategoryIds?.let {
            Gson().fromJson(it, object : TypeToken<List<Long>>() {}.type)
        } ?: emptyList()

        val periodType =
            backStackEntry.savedStateHandle.get<Int>(Util.SAVE_STATE_PERIOD_TYPE)

        val countryCode = backStackEntry.savedStateHandle.get<String>(Util.SAVE_STATE_COUNTRY_CODE)

        bottomBarState.value = false
        AddEditBudgetScreen(
            onNavigationUp = { navController.navigateUp() },
            onSave = { _, id, period, month, year ->
                navController.popBackStack()
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_PERIOD_TYPE,
                    period
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_BUDGET_ID,
                    id
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_BUDGET_MONTH,
                    month
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_BUDGET_YEAR,
                    year
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_BUDGET_ADD_SUCCESS,
                    true
                )
            },
            onSelectCategory = { selectedIds ->
                backStackEntry
                    .savedStateHandle[Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST] =
                    Gson().toJson(selectedIds)
                navController.navigate(DialogNav.MULTI_SELECT_CATEGORY.route)
            },
            selectedCategoryIds = categoryIds,
            selectedPeriodType = periodType ?: 1,
            currencyCountryCode = countryCode,
            onCurrencyChange = { code ->
                navController.navigate(DialogNav.COUNTRY_PICKER.route)

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SELECT_COUNTRY_CODE,
                    code
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SHOW_CURRENCY,
                    true
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SAVABLE_DIALOG, false
                )
            },
        )
    }
}