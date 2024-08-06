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
import com.indie.apps.pannypal.presentation.ui.navigation.MerchantNav
import com.indie.apps.pannypal.presentation.ui.screen.MerchantDataScreen
import com.indie.apps.pannypal.presentation.ui.screen.MerchantProfileScreen
import com.indie.apps.pannypal.presentation.ui.screen.MerchantScreen
import com.indie.apps.pannypal.presentation.ui.screen.NewItemScreen

fun NavGraphBuilder.MerchantRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    navigation(
        startDestination = MerchantNav.START.route,
        route = BottomNavItem.MERCHANTS.route
    ) {
        composable(route = MerchantNav.START.route) { backStackEntry ->
            // get data passed back from B
            val isEditAddSuccess: Boolean? = backStackEntry
                .savedStateHandle
                .get<Boolean>(Util.SAVE_STATE_ADD_EDIT_SUCCESS)

            backStackEntry
                .savedStateHandle
                .remove<Boolean>(Util.SAVE_STATE_ADD_EDIT_SUCCESS)

            bottomBarState.value = true
            MerchantScreen(
                isEditAddSuccess = isEditAddSuccess ?: false,
                onMerchantClick = {
                    navController.navigate(
                        MerchantNav.DATA.route.replace(
                            "{${Util.PARAM_MERCHANT_ID}}",
                            it.toString()
                        )
                    )
                },
                onAddClick = { navController.navigate(DialogNav.ADD_EDIT_MERCHANT.route) },
                onEditClick = {

                    navController.navigate(DialogNav.ADD_EDIT_MERCHANT.route)

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(Util.SAVE_STATE_EDIT_ID, it)
                }
            )
        }
        composable(route = MerchantNav.DATA.route) {
            bottomBarState.value = false
            MerchantDataScreen(
                onProfileClick = {
                    navController.navigate(MerchantNav.PROFILE.route.replace(
                        "{${Util.PARAM_MERCHANT_ID}}",
                        it.toString()
                    ))
                                 },
                onNavigationUp = { navController.navigateUp() },
                onEditClick = {
                    println("aaaaaa edit merchant")
                    navController.navigate(
                        MerchantNav.EDIT_DATA.route.replace(
                            "{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}",
                            it.toString()
                        )
                    )
                }
            )
        }

        composable(route = MerchantNav.PROFILE.route) {
            bottomBarState.value = false
            MerchantProfileScreen(
                onNavigationUp = { navController.navigateUp() })
        }

        composable(route = MerchantNav.EDIT_DATA.route) { backStackEntry ->
            bottomBarState.value = false
            val context = LocalContext.current
            val merchantDataSaveToast =
                stringResource(id = R.string.merchant_data_edit_success_message)

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
                merchant = merchant,
                payment = payment,
                onSaveSuccess = {
                    Toast.makeText(context, merchantDataSaveToast, Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }
            )
        }
    }
}