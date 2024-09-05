package com.indie.apps.pennypal.presentation.ui.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.gson.Gson
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.screen.merchant_data.MerchantDataScreen
import com.indie.apps.pennypal.presentation.ui.screen.merchant_profile.MerchantProfileScreen
import com.indie.apps.pennypal.presentation.ui.screen.merchant.MerchantScreen
import com.indie.apps.pennypal.presentation.ui.screen.new_item.NewItemScreen
import com.indie.apps.pennypal.presentation.ui.screen.setting.SettingScreen
import com.indie.apps.pennypal.util.Util

fun NavGraphBuilder.settingRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    navigation(
        startDestination = ScreenNav.SETTING_START.route,
        route = BottomNavItem.SETTING.route
    ) {
        composable(route = ScreenNav.SETTING_START.route) { backStackEntry ->
            SettingScreen(onNavigationUp = {})
        }
    }
}