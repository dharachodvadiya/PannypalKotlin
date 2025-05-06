package com.indie.apps.pennypal.presentation.ui.screen.new_item

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.util.Util

/*internal fun NavGraphBuilder.navigateToAddItemScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>
) {
    composable(route = ScreenNav.NEW_ITEM.route) { backStackEntry ->

        val savedStateHandle = backStackEntry.savedStateHandle

        val context = LocalContext.current
        val merchantDataSaveToast =
            stringResource(id = R.string.merchant_data_save_success_message)

        val isMerchantLock: Boolean? =
            savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_LOCK)

        // get data passed back from B
        val gsonStringMerchant: String? =
            savedStateHandle.get<String>(Util.SAVE_STATE_MERCHANT_NAME_DESC)

        val merchant: MerchantNameAndDetails? = if (gsonStringMerchant != null) {
            Gson().fromJson(gsonStringMerchant, MerchantNameAndDetails::class.java)
        } else null

        val gsonStringPayment: String? = savedStateHandle.get<String>(Util.SAVE_STATE_PAYMENT)

        val payment: Payment? = if (gsonStringPayment != null) {
            Gson().fromJson(gsonStringPayment, Payment::class.java)
        } else null

        val gsonStringCategory = savedStateHandle.get<String>(Util.SAVE_STATE_CATEGORY)

        val category: Category? = if (gsonStringCategory != null) {
            Gson().fromJson(gsonStringCategory, Category::class.java)
        } else null


        //backStackEntry.savedStateHandle.remove<String>(Util.SAVE_STATE_MERCHANT_NAME_DESC)
        //backStackEntry.savedStateHandle.remove<String>(Util.SAVE_STATE_PAYMENT)
        //backStackEntry.savedStateHandle.remove<String>(Util.SAVE_STATE_CATEGORY)

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
                if (currentId != null) {
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
            isMerchantLock = isMerchantLock ?: false,
            onSaveSuccess = { _, merchantDataId, merchantId ->
                context.showToast(merchantDataSaveToast)

                navController.previousBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID, merchantDataId
                )

                navController.previousBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID, merchantId
                )
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS, true
                )


                navController.popBackStack()
                //navController.navigateUp()
            })
    }
}*/

internal fun NavGraphBuilder.navigateToEditItemScreen(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    innerPadding: PaddingValues
) {
    composable(route = ScreenNav.ADD_EDIT_MERCHANT_DATA.route) { backStackEntry ->
        bottomBarState.value = false
        /*val context = LocalContext.current
        val merchantDataSaveToast =
            stringResource(id = R.string.merchant_data_edit_success_message)*/

        val savedStateHandle = backStackEntry.savedStateHandle
        // get data passed back from B
        val gsonStringMerchant =
            savedStateHandle.get<String>(Util.SAVE_STATE_MERCHANT_NAME_DESC)

        val merchant: MerchantNameAndDetails? = if (gsonStringMerchant != null) {
            Gson().fromJson(gsonStringMerchant, MerchantNameAndDetails::class.java)
        } else null

        val countryCode = savedStateHandle.get<String>(Util.SAVE_STATE_COUNTRY_CODE)

        val gsonStringPayment = savedStateHandle.get<String>(Util.SAVE_STATE_PAYMENT)

        val payment: Payment? = if (gsonStringPayment != null) {
            Gson().fromJson(gsonStringPayment, Payment::class.java)
        } else null

        val gsonStringCategory = savedStateHandle.get<String>(Util.SAVE_STATE_CATEGORY)

        val category: Category? = if (gsonStringCategory != null) {
            Gson().fromJson(gsonStringCategory, Category::class.java)
        } else null

        val isMerchantLock: Boolean? =
            savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_LOCK)


        //savedStateHandle.remove<String>(Util.SAVE_STATE_MERCHANT_NAME_DESC)
        //savedStateHandle.remove<String>(Util.SAVE_STATE_PAYMENT)
        //savedStateHandle.remove<String>(Util.SAVE_STATE_CATEGORY)

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
                if (currentId != null) {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_SELECT_CATEGORY_ID, currentId
                    )
                }

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_CATEGORY_TYPE, categoryType
                )
            },
            onCurrencyChange = { countryCode ->
                navController.navigate(DialogNav.COUNTRY_PICKER.route)

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SELECT_COUNTRY_CODE,
                    countryCode
                )

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SHOW_CURRENCY,
                    true
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_SAVABLE_DIALOG, false
                )
            },
            currencyCountryCode = countryCode,
            merchantData = merchant,
            paymentData = payment,
            categoryData = category,
            isMerchantLock = isMerchantLock ?: false,
            onSaveSuccess = { isEdit, merchantDataId, merchantId ->
                /*navController.navigateUp()*/
                //context.showToast(merchantDataSaveToast)

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
            },
            onDeleteSuccess = { merchantDataId ->

                navController.previousBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID, merchantDataId
                )

                navController.previousBackStackEntry?.savedStateHandle?.set(
                    Util.SAVE_STATE_MERCHANT_DATA_DELETE_SUCCESS, true
                )
                navController.popBackStack()
            })
    }
}