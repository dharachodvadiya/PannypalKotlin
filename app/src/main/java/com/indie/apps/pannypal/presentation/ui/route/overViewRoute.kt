package com.indie.apps.pannypal.presentation.ui.route

import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.gson.Gson
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.Payment
import com.indie.apps.pannypal.data.module.MerchantNameAndDetails
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pannypal.presentation.ui.navigation.OverviewNav
import com.indie.apps.pannypal.presentation.ui.screen.NewItemScreen
import com.indie.apps.pannypal.presentation.ui.screen.OverViewStartScreen
import com.indie.apps.pannypal.presentation.ui.screen.ProfileScreen


fun NavGraphBuilder.overViewRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {

    navigation(
        startDestination = OverviewNav.START.route, route = BottomNavItem.OVERVIEW.route
    ) {
        composable(route = OverviewNav.START.route) {
            bottomBarState.value = true
            OverViewStartScreen(onNewEntry = { navController.navigate(OverviewNav.NEW_ITEM.route) },
                onProfileClick = { navController.navigate(OverviewNav.PROFILE.route) })
        }
        composable(route = OverviewNav.NEW_ITEM.route)
        { backStackEntry ->

            val context = LocalContext.current
            val merchantDataSaveToast = stringResource(id = R.string.merchant_data_save_success_message)

            // get data passed back from B
            val gsonStringMerchant: String? = backStackEntry
                .savedStateHandle
                .get<String>(Util.SAVE_STATE_MERCHANT_NAME_DESC)

            val merchant: MerchantNameAndDetails? =
                if (gsonStringMerchant != null) {
                    Gson().fromJson(gsonStringMerchant, MerchantNameAndDetails::class.java)
                } else null

            val gsonStringPayment: String? = backStackEntry
                .savedStateHandle
                .get<String>(Util.SAVE_STATE_PAYMENT)

            val payment: Payment? =
                if (gsonStringPayment != null) {
                    Gson().fromJson(gsonStringPayment, Payment::class.java)
                } else null


            backStackEntry.savedStateHandle.remove<String>(Util.SAVE_STATE_MERCHANT_NAME_DESC)
            backStackEntry.savedStateHandle.remove<String>(Util.SAVE_STATE_PAYMENT)

            bottomBarState.value = false
            NewItemScreen(
                onNavigationUp = { navController.navigateUp() },
                onMerchantSelect = { navController.navigate(DialogNav.SELECT_MERCHANT.route) },
                onPaymentAdd = { navController.navigate(DialogNav.ADD_PAYMENT.route) },
                merchantData = merchant,
                paymentData = payment,
                onSaveSuccess = {
                    Toast.makeText(context, merchantDataSaveToast, Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }
            )
        }
        composable(route = OverviewNav.PROFILE.route) {
            bottomBarState.value = false
            ProfileScreen(onNavigationUp = { navController.navigateUp() })
        }
    }
}