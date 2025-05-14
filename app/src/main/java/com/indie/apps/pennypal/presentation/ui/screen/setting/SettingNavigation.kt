package com.indie.apps.pennypal.presentation.ui.screen.setting

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToSettingScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    composable(route = ScreenNav.SETTING.route) {
        bottomBarState.value = true
        SettingScreen(
            onDefaultPaymentChange = { currentId ->
                navController.navigate(DialogNav.SELECT_PAYMENT.route)
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SELECT_PAYMENT_ID, currentId
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SAVABLE_DIALOG, true
                )
            },
            onBalanceViewChange = {
                navController.navigate(DialogNav.SELECT_BALANCE_VIEW.route)
            },
            bottomPadding = innerPadding,
            onCurrencyChange = { currencyCode ->
                navController.navigate(DialogNav.COUNTRY_PICKER.route)

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SELECT_COUNTRY_CODE,
                    currencyCode
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SHOW_CURRENCY,
                    true
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SAVABLE_DIALOG, true
                )
            },
            onLanguageChange = {
                navController.navigate(DialogNav.SELECT_LANGUAGE.route)
            },
            onNavigationUp = {
                //navController.popBackStack()
                navController.navigate(BottomNavItem.OVERVIEW.route) {
                    launchSingleTop = true
                }
            },
            onTransaction = {
                navController.navigate(ScreenNav.SEE_ALL_DATA.route)
            },
            onMerchants = {
                navController.navigate(ScreenNav.MERCHANT.route)
            },
            onCategories = {
                navController.navigate(ScreenNav.CATEGORY.route)
            },
            onBudget = {
                navController.navigate(ScreenNav.BUDGET.route)
            },
            onPurchase = {
                navController.navigate(ScreenNav.PURCHASE.route)
            }
        )
    }
}