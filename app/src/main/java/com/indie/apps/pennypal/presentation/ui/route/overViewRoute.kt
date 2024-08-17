package com.indie.apps.pennypal.presentation.ui.route

import android.widget.Toast
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
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.OverviewNav
import com.indie.apps.pennypal.presentation.ui.screen.NewItemScreen
import com.indie.apps.pennypal.presentation.ui.screen.OverViewStartScreen
import com.indie.apps.pennypal.presentation.ui.screen.ProfileScreen
import com.indie.apps.pennypal.util.Util


fun NavGraphBuilder.overViewRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {

    navigation(
        startDestination = OverviewNav.START.route, route = BottomNavItem.OVERVIEW.route
    ) {
        composable(route = OverviewNav.START.route) { backStackEntry ->

            val merchantDataId: Long? = backStackEntry
                .savedStateHandle
                .get<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)

            backStackEntry
                .savedStateHandle
                .remove<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)

            backStackEntry
                .savedStateHandle
                .remove<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)

            backStackEntry
                .savedStateHandle
                .remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_SUCCESS)

            bottomBarState.value = true
            OverViewStartScreen(
                onNewEntry = { navController.navigate(OverviewNav.NEW_ITEM.route) },
                onProfileClick = { navController.navigate(OverviewNav.PROFILE.route) },
                bottomPadding = innerPadding,
                addEditMerchantDataId = merchantDataId ?: -1
            )
        }
        composable(route = OverviewNav.NEW_ITEM.route)
        { backStackEntry ->

            val context = LocalContext.current
            val merchantDataSaveToast =
                stringResource(id = R.string.merchant_data_save_success_message)

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
                onSaveSuccess = { isEdit, merchantDataId, merchantId ->
                    Toast.makeText(context, merchantDataSaveToast, Toast.LENGTH_SHORT).show()

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID,
                            merchantDataId
                        )

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID,
                            merchantId
                        )
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_SUCCESS,
                            true
                        )


                    navController.popBackStack()
                    //navController.navigateUp()
                }
            )
        }
        composable(route = OverviewNav.PROFILE.route) {
            bottomBarState.value = false
            ProfileScreen(onNavigationUp = { navController.navigateUp() })
        }
    }
}