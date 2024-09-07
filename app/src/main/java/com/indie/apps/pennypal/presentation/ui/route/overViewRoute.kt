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
import com.indie.apps.pennypal.presentation.ui.screen.new_item.NewItemScreen
import com.indie.apps.pennypal.presentation.ui.screen.overview.OverViewStartScreen
import com.indie.apps.pennypal.presentation.ui.screen.profile.ProfileScreen
import com.indie.apps.pennypal.util.Util


fun NavGraphBuilder.overViewRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {

    navigation(
        startDestination = ScreenNav.OVERVIEW_START.route, route = BottomNavItem.OVERVIEW.route
    ) {
        composable(route = ScreenNav.OVERVIEW_START.route) { backStackEntry ->

            val merchantDataId: Long? = backStackEntry
                .savedStateHandle
                .get<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)

            val isAddMerchantDataSuccess: Boolean? = backStackEntry
                .savedStateHandle
                .get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)

            bottomBarState.value = true

            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)
            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)
            backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)


            OverViewStartScreen(
                onProfileClick = { navController.navigate(ScreenNav.PROFILE.route) },
                bottomPadding = innerPadding,
                addEditMerchantDataId = merchantDataId ?: -1,
                isAddMerchantDataSuccess = isAddMerchantDataSuccess ?: false
            )
        }
        composable(route = ScreenNav.NEW_ITEM.route)
        { backStackEntry ->

            val context = LocalContext.current
            val merchantDataSaveToast =
                stringResource(id = R.string.merchant_data_save_success_message)

            val isMerchantLock: Boolean? = backStackEntry
                .savedStateHandle
                .get<Boolean>(Util.SAVE_STATE_MERCHANT_LOCK)

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
                onPaymentAdd = { navController.navigate(DialogNav.ADD_EDIT_PAYMENT.route) },
                merchantData = merchant,
                paymentData = payment,
                isMerchantLock = isMerchantLock ?: false,
                onSaveSuccess = { _, merchantDataId, merchantId ->
                    context.showToast(merchantDataSaveToast)

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
                            Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS,
                            true
                        )


                    navController.popBackStack()
                    //navController.navigateUp()
                }
            )
        }
        composable(route = ScreenNav.PROFILE.route) { backStackEntry->

            val code: String? = backStackEntry
                .savedStateHandle
                .get<String>(Util.SAVE_STATE_CURRENCY_CODE)

            bottomBarState.value = false
            ProfileScreen(
                onNavigationUp = { navController.navigateUp() },
                code = code,
                onCurrencyChangeClick = {
                    navController.navigate(DialogNav.COUNTRY_PICKER.route)
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(Util.SAVE_STATE_SHOW_CURRENCY, true)
                })
        }
    }
}