package com.indie.apps.pennypal.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.entity.toMerchantNameAndDetails
import com.indie.apps.pennypal.data.module.ContactNumberAndCode
import com.indie.apps.pennypal.presentation.ui.component.showToast
import com.indie.apps.pennypal.presentation.ui.dialog.add_edit_merchant.DialogAddMerchant
import com.indie.apps.pennypal.presentation.ui.dialog.add_payment.DialogAddPayment
import com.indie.apps.pennypal.presentation.ui.dialog.country_picker.DialogCountryPicker
import com.indie.apps.pennypal.presentation.ui.dialog.search_merchant.DialogSearchMerchant
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.component.navigation.BottomNavigationBarCustom1
import com.indie.apps.pennypal.presentation.ui.dialog.contact_picker.DialogContactPicker
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.OverviewNav
import com.indie.apps.pennypal.presentation.ui.route.merchantRoute
import com.indie.apps.pennypal.presentation.ui.route.overViewRoute
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.util.Util

@Composable
fun PennyPalApp() {
    val context = LocalContext.current
    val paymentSaveToast = stringResource(id = R.string.payment_save_success_toast)
    val merchantSaveToast = stringResource(id = R.string.merchant_save_success_toast)
    val merchantEditToast = stringResource(id = R.string.merchant_edit_success_message)
    PennyPalTheme(darkTheme = true) {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = BottomNavItem.entries.find {
            currentDestination?.route?.startsWith(it.route + "/")
                ?: false
        }
            ?: BottomNavItem.OVERVIEW

        // State of bottomBar, set state to false, if current page route is "car_details"
        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

        Scaffold(
            bottomBar = {
                BottomNavigationBarCustom1(
                    tabs = BottomNavItem.entries.toTypedArray(), onTabSelected = { newScreen ->

                        if (newScreen.route != currentScreen.route) {
                            navController.navigate(newScreen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(newScreen.route)
                            }
                        }
                    },
                    onAddClick = { navController.navigate(OverviewNav.NEW_ITEM.route) },
                    currentTab = currentScreen,
                    bottomBarState = bottomBarState.value
                )
            }) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = BottomNavItem.OVERVIEW.route,
                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                }
            ) {
                overViewRoute(navController, bottomBarState, innerPadding)
                merchantRoute(navController, bottomBarState, innerPadding)

                dialog(
                    route = DialogNav.SELECT_MERCHANT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogSearchMerchant(
                        onNavigationUp = {
                            navController.navigateUp()
                        },
                        onAddClick = {
                            navController.navigate(DialogNav.ADD_EDIT_MERCHANT.route) {
                                navController.popBackStack()
                            }
                        },
                        onSelectMerchant = {
                            if (it != null)
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_MERCHANT_NAME_DESC, Gson().toJson(it))

                            navController.popBackStack()
                        }
                    )
                }
                dialog(
                    route = DialogNav.ADD_EDIT_MERCHANT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) { backStackEntry ->
                    // get data passed back from B
                    val dialCode: String? = backStackEntry
                        .savedStateHandle
                        .get<String>(Util.SAVE_STATE_COUNTRY_DIAL_CODE)

                    val gsonStringContactData: String? = backStackEntry
                        .savedStateHandle
                        .get<String>(Util.SAVE_STATE_CONTACT_NUMBER_DIAL_CODE)

                    val contactData: ContactNumberAndCode? =
                        if (gsonStringContactData != null) {
                            Gson().fromJson(gsonStringContactData, ContactNumberAndCode::class.java)
                        } else null

                    val editId: Long? = backStackEntry
                        .savedStateHandle
                        .get<Long>(Util.SAVE_STATE_EDIT_ID)

                    DialogAddMerchant(
                        onNavigationUp = { navController.navigateUp() },
                        onSaveSuccess = { merchant, isEdit ->
                            //navController.navigateUp()
                            context.showToast(if (isEdit) merchantEditToast else merchantSaveToast)

                            if (merchant != null) {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(
                                        Util.SAVE_STATE_MERCHANT_NAME_DESC,
                                        Gson().toJson(merchant.toMerchantNameAndDetails())
                                    )

                                if (isEdit) {
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set(Util.SAVE_STATE_EDIT_SUCCESS, true)
                                } else {
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set(Util.SAVE_STATE_ADD_SUCCESS, true)
                                }



                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_ADD_EDIT_SUCCESS_ID, merchant.id)
                            }

                            navController.popBackStack()
                        },
                        onCpp = {
                            navController.navigate(DialogNav.COUNTRY_PICKER.route)
                        },
                        onContactBook = {
                            navController.navigate(DialogNav.CONTACT_PICKER.route)
                        },
                        code = dialCode,
                        editId = editId,
                        contactNumberAndCode = contactData
                    )
                }
                dialog(
                    route = DialogNav.ADD_PAYMENT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogAddPayment(
                        onNavigationUp = { navController.navigateUp() },
                        onSaveSuccess = {
                            //navController.navigateUp()
                            context.showToast(paymentSaveToast)

                            if (it != null)
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_PAYMENT, Gson().toJson(it))

                            navController.popBackStack()
                        }
                    )
                }
                dialog(
                    route = DialogNav.COUNTRY_PICKER.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) { backStackEntry ->

                    val isShowCurrency: Boolean? = backStackEntry
                        .savedStateHandle
                        .get<Boolean>(Util.SAVE_STATE_SHOW_CURRENCY)

                    DialogCountryPicker(
                        onNavigationUp = { navController.navigateUp() },
                        onSelect = {
                            // Pass data back to A
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(Util.SAVE_STATE_COUNTRY_DIAL_CODE, it.dialCode)
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(Util.SAVE_STATE_CURRENCY_CODE, it.currencyCode)


                            navController.popBackStack()
                        },
                        isShowCurrency = isShowCurrency ?: false,
                    )
                }
                dialog(
                    route = DialogNav.CONTACT_PICKER.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogContactPicker(
                        onNavigationUp = { navController.navigateUp() },
                        onSelect = {
                            // Pass data back to A
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(Util.SAVE_STATE_CONTACT_NUMBER_DIAL_CODE, Gson().toJson(it))

                            navController.popBackStack()
                        }
                    )
                }
            }

        }
    }

}

@Preview
@Composable
private fun MainScreenPreview() {
    PennyPalTheme(darkTheme = true) {
        PennyPalApp()
    }
}