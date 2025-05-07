package com.indie.apps.pennypal.presentation.ui.navigation.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.indie.apps.pennypal.presentation.ui.screen.add_budget.navigateToAddEditBudgetScreen
import com.indie.apps.pennypal.presentation.ui.screen.all_data.navigateToAllDataScreen
import com.indie.apps.pennypal.presentation.ui.screen.budget.navigateToBudgetScreen
import com.indie.apps.pennypal.presentation.ui.screen.budget_filter.navigateToBudgetFilterScreen
import com.indie.apps.pennypal.presentation.ui.screen.category.navigateToCategoryScreen
import com.indie.apps.pennypal.presentation.ui.screen.merchant.navigateToMerchantScreen
import com.indie.apps.pennypal.presentation.ui.screen.merchant_data.navigateToMerchantDataScreen
import com.indie.apps.pennypal.presentation.ui.screen.merchant_profile.navigateToMerchantProfileScreen
import com.indie.apps.pennypal.presentation.ui.screen.new_item.navigateToEditItemScreen
import com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis.navigateToSingleBudgetScreen

fun NavGraphBuilder.otherRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {

    //transaction Item
    navigateToEditItemScreen(navController, bottomBarState, innerPadding)
    navigateToAllDataScreen(navController, bottomBarState, innerPadding)

    //budget
    navigateToBudgetScreen(navController, bottomBarState, innerPadding)
    navigateToSingleBudgetScreen(navController, bottomBarState, innerPadding)
    //navigateToAddBudgetScreen(navController, bottomBarState)
    navigateToAddEditBudgetScreen(navController, bottomBarState, innerPadding)
    navigateToBudgetFilterScreen(navController, bottomBarState, innerPadding)

    //category
    navigateToCategoryScreen(navController, bottomBarState, innerPadding)

    //merchant
    navigateToMerchantScreen(navController, bottomBarState, innerPadding)
    navigateToMerchantDataScreen(navController, bottomBarState, innerPadding)
    navigateToMerchantProfileScreen(navController, bottomBarState, innerPadding)
}