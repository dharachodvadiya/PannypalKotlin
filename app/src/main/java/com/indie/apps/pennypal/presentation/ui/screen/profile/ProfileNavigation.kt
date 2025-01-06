package com.indie.apps.pennypal.presentation.ui.screen.profile

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

internal fun NavGraphBuilder.navigateToProfileScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.PROFILE.route) { backStackEntry ->

        val code: String? =
            backStackEntry.savedStateHandle.get<String>(Util.SAVE_STATE_COUNTRY_CODE)

        bottomBarState.value = false
        ProfileScreen(onNavigationUp = { navController.navigateUp() },
            code = code,
            onCurrencyChangeClick = {
                navController.navigate(DialogNav.COUNTRY_PICKER.route)
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SHOW_CURRENCY,
                    true
                )
            })
    }
}