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
import com.indie.apps.pennypal.data.entity.Category
import com.indie.apps.pennypal.data.entity.Payment
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.screen.merchant.MerchantScreen
import com.indie.apps.pennypal.presentation.ui.screen.merchant_data.MerchantDataScreen
import com.indie.apps.pennypal.presentation.ui.screen.merchant_profile.MerchantProfileScreen
import com.indie.apps.pennypal.presentation.ui.screen.new_item.NewItemScreen
import com.indie.apps.pennypal.util.Util

fun NavGraphBuilder.merchantRoute(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    navigation(
        startDestination = ScreenNav.MERCHANT_START.route, route = BottomNavItem.MERCHANTS.route
    ) {
        composable(route = ScreenNav.MERCHANT_START.route) { backStackEntry ->

            val savedStateHandle = backStackEntry.savedStateHandle
            // get data passed back from B
            val isAddSuccess = savedStateHandle.get<Boolean>(Util.SAVE_STATE_ADD_MERCHANT_SUCCESS)

            val isEditSuccess = savedStateHandle.get<Boolean>(Util.SAVE_STATE_EDIT_MERCHANT_SUCCESS)

            val editAddId = savedStateHandle.get<Long>(Util.SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID)

            val isAddMerchantDataSuccess =
                savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)

            val merchantId = savedStateHandle.get<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)

            savedStateHandle.remove<Boolean>(Util.SAVE_STATE_EDIT_MERCHANT_SUCCESS)
            savedStateHandle.remove<Boolean>(Util.SAVE_STATE_ADD_MERCHANT_SUCCESS)
            savedStateHandle.remove<Long>(Util.SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID)

            savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)
            savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)
            savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)

            bottomBarState.value = true
            MerchantScreen(
                isAddSuccess = isAddSuccess ?: false,
                isEditSuccess = isEditSuccess ?: false,
                editAddId = editAddId ?: -1,
                onMerchantClick = {
                    navController.navigate(
                        ScreenNav.MERCHANT_DATA.route.replace(
                            "{${Util.PARAM_MERCHANT_ID}}", it.toString()
                        )
                    )
                },
                onAddClick = { navController.navigate(DialogNav.ADD_EDIT_MERCHANT.route) },
                onEditClick = {

                    navController.navigate(DialogNav.ADD_EDIT_MERCHANT.route)

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_MERCHANT_EDIT_ID,
                        it
                    )
                },
                bottomPadding = innerPadding,
                isAddMerchantDataSuccess = isAddMerchantDataSuccess ?: false,
                merchantId = merchantId ?: -1
            )
        }
        composable(route = ScreenNav.MERCHANT_DATA.route) { backStackEntry ->

            val merchantDataId: Long? =
                backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)

            val isEditMerchantDataSuccess: Boolean? =
                backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS)

            val isAddMerchantDataSuccess: Boolean? =
                backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)

            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)
            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)
            backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS)
            backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)

            bottomBarState.value = false
            MerchantDataScreen(
                onProfileClick = {
                    navController.navigate(
                        ScreenNav.MERCHANT_PROFILE.route.replace(
                            "{${Util.PARAM_MERCHANT_ID}}", it.toString()
                        )
                    )
                },
                onNavigationUp = { navController.navigateUp() },
                onEditClick = {
                    navController.navigate(
                        ScreenNav.EDIT_MERCHANT_DATA.route.replace(
                            "{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}", it.toString()
                        )
                    )
                },
                onAddClick = {
                    navController.navigate(ScreenNav.NEW_ITEM.route)

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_MERCHANT_NAME_DESC,
                        Gson().toJson(it)
                    )

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_MERCHANT_LOCK,
                        true
                    )

                },
                isEditSuccess = isEditMerchantDataSuccess ?: false,
                isAddSuccess = isAddMerchantDataSuccess ?: false,
                merchantDataId = merchantDataId ?: -1
            )
        }

        composable(route = ScreenNav.MERCHANT_PROFILE.route) {
            bottomBarState.value = false
            MerchantProfileScreen(onNavigationUp = { navController.navigateUp() })
        }

        composable(route = ScreenNav.EDIT_MERCHANT_DATA.route) { backStackEntry ->
            bottomBarState.value = false
            val context = LocalContext.current
            val merchantDataSaveToast =
                stringResource(id = R.string.merchant_data_edit_success_message)

            val savedStateHandle = backStackEntry.savedStateHandle
            // get data passed back from B
            val gsonStringMerchant =
                savedStateHandle.get<String>(Util.SAVE_STATE_MERCHANT_NAME_DESC)

            val merchant: MerchantNameAndDetails? = if (gsonStringMerchant != null) {
                Gson().fromJson(gsonStringMerchant, MerchantNameAndDetails::class.java)
            } else null

            val gsonStringPayment = savedStateHandle.get<String>(Util.SAVE_STATE_PAYMENT)

            val payment: Payment? = if (gsonStringPayment != null) {
                Gson().fromJson(gsonStringPayment, Payment::class.java)
            } else null

            val gsonStringCategory = savedStateHandle.get<String>(Util.SAVE_STATE_CATEGORY)

            val category: Category? = if (gsonStringCategory != null) {
                Gson().fromJson(gsonStringCategory, Category::class.java)
            } else null


            savedStateHandle.remove<String>(Util.SAVE_STATE_MERCHANT_NAME_DESC)
            savedStateHandle.remove<String>(Util.SAVE_STATE_PAYMENT)
            savedStateHandle.remove<String>(Util.SAVE_STATE_CATEGORY)

            bottomBarState.value = false
            NewItemScreen(
                onNavigationUp = { navController.navigateUp() },
                onMerchantSelect = { navController.navigate(DialogNav.SELECT_MERCHANT.route) },
                onPaymentSelect = { currentId ->
                    navController.navigate(DialogNav.SELECT_PAYMENT.route)
                    if (currentId != null) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            Util.SAVE_STATE_SELECT_PAYMENT_ID, currentId
                        )
                    }
                },
                onCategorySelect = { currentId, categoryType ->
                    navController.navigate(DialogNav.SELECT_CATEGORY.route)
                    if(currentId != null) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            Util.SAVE_STATE_SELECT_CATEGORY_ID, currentId
                        )
                    }

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_CATEGORY_TYPE, categoryType
                    )
                },
                merchantData = merchant,
                paymentData = payment,
                categoryData = category,
                isMerchantLock = false,
                onSaveSuccess = { isEdit, merchantDataId, merchantId ->
                    /*navController.navigateUp()*/
                    context.showToast(merchantDataSaveToast)

                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID, merchantDataId
                    )

                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID, merchantId
                    )
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS, isEdit
                    )

                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS, !isEdit
                    )
                    navController.popBackStack()
                })
        }
    }
}