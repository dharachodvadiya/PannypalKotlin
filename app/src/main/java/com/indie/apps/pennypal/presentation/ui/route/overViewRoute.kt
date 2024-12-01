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
import com.google.gson.reflect.TypeToken
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.database.entity.Category
import com.indie.apps.pennypal.data.database.entity.Payment
import com.indie.apps.pennypal.data.module.MerchantNameAndDetails
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.screen.add_budget.AddBudgetScreen
import com.indie.apps.pennypal.presentation.ui.screen.all_data.AllDataScreen
import com.indie.apps.pennypal.presentation.ui.screen.budget.BudgetScreen
import com.indie.apps.pennypal.presentation.ui.screen.new_item.NewItemScreen
import com.indie.apps.pennypal.presentation.ui.screen.overview.OverViewStartScreen
import com.indie.apps.pennypal.presentation.ui.screen.overview_analysis.OverViewAnalysisScreen
import com.indie.apps.pennypal.presentation.ui.screen.profile.ProfileScreen
import com.indie.apps.pennypal.presentation.ui.screen.single_budget_analysis.SingleBudgetScreen
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

            val merchantDataId: Long? =
                backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)

            val merchantId: Long? =
                backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID)

            val isAddMerchantDataSuccess: Boolean? =
                backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)

            val isEditMerchantDataSuccess: Boolean? =
                backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS)

            bottomBarState.value = true

            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_ADD_EDIT_ID)
            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID)
            backStackEntry.savedStateHandle.remove<Long>(Util.SAVE_STATE_MERCHANT_DATA_ADD_EDIT_ID)
            backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_ADD_SUCCESS)
            backStackEntry.savedStateHandle.remove<Boolean>(Util.SAVE_STATE_MERCHANT_DATA_EDIT_SUCCESS)


            OverViewStartScreen(
                onProfileClick = { navController.navigate(ScreenNav.PROFILE.route) },
                addEditMerchantDataId = merchantDataId ?: -1,
                isAddMerchantDataSuccess = isAddMerchantDataSuccess ?: false,
                isEditSuccess = isEditMerchantDataSuccess ?: false,
                onSeeAllTransactionClick = {
                    navController.navigate(ScreenNav.SEE_ALL_DATA.route)
                },
                onNavigationUp = {
                    navController.popBackStack()
                },
                onSeeAllMerchantClick = {
                    navController.navigate(BottomNavItem.MERCHANTS.route)
                },
                onExploreAnalysisClick = {
                    navController.navigate(ScreenNav.OVERVIEW_ANALYSIS.route)
                },
                onTransactionClick = {
                    navController.navigate(
                        ScreenNav.EDIT_MERCHANT_DATA.route.replace(
                            "{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}", it.toString()
                        )
                    )
                },
                onExploreBudgetClick = {
                    navController.navigate(ScreenNav.BUDGET.route)
                },
                onAddMerchant = {
                    navController.navigate(DialogNav.ADD_EDIT_MERCHANT.route)
                },
                addMerchantId = merchantId ?: -1L,
                onSetBudgetClick = {
                    navController.navigate(ScreenNav.ADD_BUDGET.route)

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_PERIOD_TYPE,
                        it
                    )
                }
            )
        }
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
        composable(route = ScreenNav.SEE_ALL_DATA.route) { backStackEntry ->

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
            AllDataScreen(
                onDataClick = {
                    navController.navigate(
                        ScreenNav.EDIT_MERCHANT_DATA.route.replace(
                            "{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}", it.toString()
                        )
                    )
                },
                onAddClick = {
                    navController.navigate(ScreenNav.NEW_ITEM.route)
                },
                onNavigationUp = {
                    navController.navigateUp()
                },
                editAddId = merchantDataId ?: -1,
                isEditSuccess = isEditMerchantDataSuccess ?: false,
                isAddSuccess = isAddMerchantDataSuccess ?: false,
                bottomPadding = innerPadding,
            )
        }
        composable(route = ScreenNav.OVERVIEW_ANALYSIS.route) {
            bottomBarState.value = false
            OverViewAnalysisScreen(
                onNavigationUp = { navController.navigateUp() }
            )
        }

        composable(route = ScreenNav.BUDGET.route) {
            bottomBarState.value = false
            BudgetScreen(
                onNavigationUp = { navController.navigateUp() },
                onAddClick = {
                    navController.navigate(ScreenNav.ADD_BUDGET.route)

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        Util.SAVE_STATE_PERIOD_TYPE,
                        it
                    )
                },
                onBudgetEditClick = {
                    navController.navigate(
                        ScreenNav.SINGLE_BUDGET_ANALYSIS.route.replace(
                            "{${Util.PARAM_BUDGET_ID}}", it.toString()
                        )
                    )

                    /* navController.navigate(
                         ScreenNav.EDIT_BUDGET.route.replace(
                             "{${Util.PARAM_BUDGET_ID}}", it.toString()
                         )
                     )*/
                }
            )
        }

        composable(route = ScreenNav.SINGLE_BUDGET_ANALYSIS.route) {
            bottomBarState.value = false
            SingleBudgetScreen(
                onNavigationUp = { navController.navigateUp() },
                onEditClick = {
                    navController.navigate(
                        ScreenNav.EDIT_BUDGET.route.replace(
                            "{${Util.PARAM_BUDGET_ID}}", it.toString()
                        )
                    )
                },
                onDeleteSuccess = {
                    navController.navigateUp()
                }
            )
        }

        composable(route = ScreenNav.ADD_BUDGET.route) { backStackEntry ->

            val gsonStringCategoryIds =
                backStackEntry.savedStateHandle.get<String>(Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST)

            val categoryIds: List<Long> = gsonStringCategoryIds?.let {
                Gson().fromJson(it, object : TypeToken<List<Long>>() {}.type)
            } ?: emptyList()

            val periodType =
                backStackEntry.savedStateHandle.get<Int>(Util.SAVE_STATE_PERIOD_TYPE)

            bottomBarState.value = false
            AddBudgetScreen(
                onNavigationUp = { navController.navigateUp() },
                onSave = { _, _ ->
                    navController.popBackStack()
                },
                onSelectCategory = { selectedIds ->
                    backStackEntry
                        ?.savedStateHandle
                        ?.set(
                            Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST,
                            Gson().toJson(selectedIds)
                        )
                    navController.navigate(DialogNav.MULTI_SELECT_CATEGORY.route)
                },
                selectedCategoryIds = categoryIds,
                selectedPeriodType = periodType ?: 1
            )
        }

        composable(route = ScreenNav.EDIT_BUDGET.route) { backStackEntry ->

            val gsonStringCategoryIds =
                backStackEntry.savedStateHandle.get<String>(Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST)

            val categoryIds: List<Long> = gsonStringCategoryIds?.let {
                Gson().fromJson(it, object : TypeToken<List<Long>>() {}.type)
            } ?: emptyList()

            bottomBarState.value = false
            AddBudgetScreen(
                onNavigationUp = { navController.navigateUp() },
                onSave = { _, _ ->
                    navController.popBackStack()
                },
                onSelectCategory = { selectedIds ->
                    backStackEntry
                        ?.savedStateHandle
                        ?.set(
                            Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST,
                            Gson().toJson(selectedIds)
                        )
                    navController.navigate(DialogNav.MULTI_SELECT_CATEGORY.route)
                },
                selectedCategoryIds = categoryIds,
                selectedPeriodType = 1
            )
        }

    }
}