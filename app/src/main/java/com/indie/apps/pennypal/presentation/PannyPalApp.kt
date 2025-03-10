package com.indie.apps.pennypal.presentation

import DialogAddEditCategory
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.indie.apps.pennypal.data.database.entity.toMerchantNameAndDetails
import com.indie.apps.pennypal.data.module.ContactNumberAndCode
import com.indie.apps.pennypal.presentation.ui.component.clickableWithNoRipple
import com.indie.apps.pennypal.presentation.ui.component.navigation.BottomNavigationBarCustom1
import com.indie.apps.pennypal.presentation.ui.component.roundedCornerBackground
import com.indie.apps.pennypal.presentation.ui.dialog.add_edit_merchant.DialogAddMerchant
import com.indie.apps.pennypal.presentation.ui.dialog.add_edit_payment.DialogAddPayment
import com.indie.apps.pennypal.presentation.ui.dialog.contact_picker.DialogContactPicker
import com.indie.apps.pennypal.presentation.ui.dialog.country_picker.DialogCountryPicker
import com.indie.apps.pennypal.presentation.ui.dialog.multi_select_Category.DialogMultiSelectCategory
import com.indie.apps.pennypal.presentation.ui.dialog.search_merchant.DialogSearchMerchant
import com.indie.apps.pennypal.presentation.ui.dialog.select_balance_view.DialogSelectBalanceView
import com.indie.apps.pennypal.presentation.ui.dialog.select_category.DialogSelectCategory
import com.indie.apps.pennypal.presentation.ui.dialog.select_language.DialogLanguage
import com.indie.apps.pennypal.presentation.ui.dialog.select_payment.DialogSelectPayment
import com.indie.apps.pennypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pennypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pennypal.presentation.ui.navigation.ScreenNav
import com.indie.apps.pennypal.presentation.ui.route.categoryRoute
import com.indie.apps.pennypal.presentation.ui.route.merchantRoute
import com.indie.apps.pennypal.presentation.ui.route.overViewRoute
import com.indie.apps.pennypal.presentation.ui.route.paymentRoute
import com.indie.apps.pennypal.presentation.ui.route.settingRoute
import com.indie.apps.pennypal.presentation.ui.screen.on_boarding.OnBoardingScreen
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.presentation.ui.theme.PennyPalTheme
import com.indie.apps.pennypal.repository.PreferenceRepository
import com.indie.apps.pennypal.util.Util

@Composable
fun PennyPalApp(preferenceRepository: PreferenceRepository) {

    val isFirstLaunch = preferenceRepository.getBoolean(Util.PREF_NEW_INSTALL, true)
    PennyPalTheme(darkTheme = true) {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = BottomNavItem.entries.find {
            currentDestination?.route?.startsWith(it.route + "/")
                ?: false
        }
            ?: BottomNavItem.OVERVIEW

        val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

        Scaffold(
            floatingActionButton = {
                if (navController.currentDestination?.route == ScreenNav.OVERVIEW_START.route) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            //.offset { IntOffset(0, -90) }
                            //.padding(PaddingValues(bottom = 30.dp + bottomNavPadding))
                            .zIndex(1f)
                            .size(26.dp * 2)
                            .roundedCornerBackground(MyAppTheme.colors.lightBlue2)
                            .clickableWithNoRipple(onClick = {
                                navController.navigate(ScreenNav.ADD_EDIT_MERCHANT_DATA.route)
                            }, enabled = true, role = Role.Button)
                    ) {
                        Icon(Icons.Default.Add, "Add", tint = MyAppTheme.colors.black)
                    }
                }
            },
            bottomBar = {
                BottomNavigationBarCustom1(
                    tabs = BottomNavItem.entries.toTypedArray(),
                    onTabSelected = { newScreen ->
                        if (newScreen.route != currentScreen.route) {
                            navController.navigate(newScreen.route) {
                                launchSingleTop = true
                               // restoreState = true
                                //popUpTo(ScreenNav.OVERVIEW_START.route)
                            }
                        }
                    },
                    currentTab = currentScreen,
                    bottomBarState = bottomBarState.value
                )
            }) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = if (isFirstLaunch) ScreenNav.BOARDING.route else BottomNavItem.OVERVIEW.route,
                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                }
            ) {

                overViewRoute(navController, bottomBarState, innerPadding)
                categoryRoute(navController, bottomBarState, innerPadding)
                merchantRoute(navController, bottomBarState, innerPadding)
                paymentRoute(navController, bottomBarState, innerPadding)
                settingRoute(navController, bottomBarState, innerPadding)

                composable(route = ScreenNav.BOARDING.route) { backStackEntry ->
                    bottomBarState.value = false

                    val countryCode: String? =
                        backStackEntry.savedStateHandle.get<String>(Util.SAVE_STATE_COUNTRY_CODE)

                    OnBoardingScreen(
                        onBoardingComplete = {
                            bottomBarState.value = true
                            navController.navigate(ScreenNav.OVERVIEW_START.route) {
                                navController.popBackStack()
                            }
                        },
                        onCurrencyChange = {
                            navController.navigate(DialogNav.COUNTRY_PICKER.route)

                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                Util.SAVE_STATE_SHOW_CURRENCY,
                                true
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                Util.SAVE_STATE_SAVABLE_DIALOG, false
                            )
                        },
                        countryCode = countryCode
                    )
                }

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
                    val savedStateHandle = backStackEntry.savedStateHandle
                    // get data passed back from B
                    val dialCode = savedStateHandle.get<String>(Util.SAVE_STATE_COUNTRY_DIAL_CODE)

                    val gsonStringContactData =
                        savedStateHandle.get<String>(Util.SAVE_STATE_CONTACT_NUMBER_DIAL_CODE)

                    val contactData: ContactNumberAndCode? =
                        if (gsonStringContactData != null) {
                            Gson().fromJson(gsonStringContactData, ContactNumberAndCode::class.java)
                        } else null

                    DialogAddMerchant(
                        onNavigationUp = { navController.navigateUp() },
                        onSaveSuccess = { merchant, isEdit ->
                            //navController.navigateUp()
                            //context.showToast(if (isEdit) merchantEditToast else merchantSaveToast)

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
                                        ?.set(Util.SAVE_STATE_EDIT_MERCHANT_SUCCESS, true)
                                } else {
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set(Util.SAVE_STATE_ADD_MERCHANT_SUCCESS, true)

                                }



                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_ADD_EDIT_MERCHANT_SUCCESS_ID, merchant.id)
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
                        //editId = editId,
                        contactNumberAndCode = contactData
                    )
                }
                dialog(
                    route = DialogNav.ADD_EDIT_PAYMENT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) { backStackEntry ->

                    DialogAddPayment(
                        onNavigationUp = { navController.navigateUp() },
                        onSaveSuccess = { payment, isEdit ->

                            if (payment != null)
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_PAYMENT, Gson().toJson(payment))

                            if (isEdit) {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_PAYMENT_EDIT_SUCCESS, true)
                            }

                            if (payment != null) {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_PAYMENT_ADD_EDIT_ID, payment.id)
                            }

                            navController.popBackStack()
                        }
                    )
                }
                dialog(
                    route = DialogNav.ADD_EDIT_CATEGORY.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) { backStackEntry ->

                    val type =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Int>(Util.SAVE_STATE_CATEGORY_TYPE)

                    DialogAddEditCategory(
                        onNavigationUp = { navController.navigateUp() },
                        onSaveSuccess = { category, isEdit ->

                            /*if (category != null)
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_CATEGORY, Gson().toJson(category))

                            */
                            /*if (isEdit) {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_CATEGORY_EDIT_SUCCESS, true)
                            }*/

                            if (isEdit) {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_CATEGORY_EDIT_SUCCESS, true)
                            } else {

                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_CATEGORY_ADD_SUCCESS, true)

                            }
                            if (category != null) {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_CATEGORY, Gson().toJson(category))

                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_CATEGORY_ADD_EDIT_ID, category.id)
                            }

                            navController.popBackStack()
                        },
                        categoryType = type
                    )
                }
                dialog(
                    route = DialogNav.COUNTRY_PICKER.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) { backStackEntry ->

                    val isShowCurrency: Boolean? = backStackEntry
                        .savedStateHandle
                        .get<Boolean>(Util.SAVE_STATE_SHOW_CURRENCY)

                    val isSavable =
                        backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_SAVABLE_DIALOG)

                    val countryCode =
                        backStackEntry.savedStateHandle.get<String>(Util.SAVE_STATE_SELECT_COUNTRY_CODE)

                    DialogCountryPicker(
                        onNavigationUp = { navController.navigateUp() },
                        onSelect = {
                            // Pass data back to A
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(Util.SAVE_STATE_COUNTRY_DIAL_CODE, it.dialCode)
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(Util.SAVE_STATE_COUNTRY_CODE, it.countryCode)


                            navController.popBackStack()
                        },
                        onSaveSuccess = {
                            navController.popBackStack()
                        },
                        isShowCurrency = isShowCurrency ?: false,
                        isSavable = isSavable ?: false,
                        selectedCountryCode = countryCode ?: ""
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
                dialog(
                    route = DialogNav.SELECT_PAYMENT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) { backStackEntry ->

                    val currentId =
                        backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_SELECT_PAYMENT_ID)

                    val isSavable =
                        backStackEntry.savedStateHandle.get<Boolean>(Util.SAVE_STATE_SAVABLE_DIALOG)

                    DialogSelectPayment(
                        onNavigationUp = { navController.navigateUp() },
                        onSelect = { payment ->
                            // Pass data back to A

                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(Util.SAVE_STATE_PAYMENT, Gson().toJson(payment))

                            navController.popBackStack()
                        },
                        isSavable = isSavable ?: false,
                        selectedId = currentId ?: 1L,
                        onSaveSuccess = {
                            navController.popBackStack()
                        }
                    )

                }
                dialog(
                    route = DialogNav.SELECT_CATEGORY.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) { backStackEntry ->

                    val currentId =
                        backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_SELECT_CATEGORY_ID)

                    val type =
                        backStackEntry.savedStateHandle.get<Int>(Util.SAVE_STATE_CATEGORY_TYPE)
                    val addEditId =
                        backStackEntry.savedStateHandle.get<Long>(Util.SAVE_STATE_CATEGORY_ADD_EDIT_ID)

                    DialogSelectCategory(
                        onNavigationUp = { navController.navigateUp() },
                        onSelect = { category ->
                            // Pass data back to A

                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(Util.SAVE_STATE_CATEGORY, Gson().toJson(category))

                            navController.popBackStack()
                        },
                        selectedId = currentId ?: 0L,
                        type = type ?: -1,
                        onAddCategory = {
                            navController.navigate(DialogNav.ADD_EDIT_CATEGORY.route){
                                navController.popBackStack()
                            }
                        },
                        addCategoryId = addEditId ?: -1
                    )

                }
                dialog(
                    route = DialogNav.SELECT_BALANCE_VIEW.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogSelectBalanceView(
                        onSelect = { navController.popBackStack() },
                        onNavigationUp = { navController.popBackStack() }
                    )
                }

                dialog(
                    route = DialogNav.SELECT_LANGUAGE.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogLanguage(
                        onSelect = { navController.popBackStack() },
                        onNavigationUp = { navController.popBackStack() }
                    )
                }

                dialog(
                    route = DialogNav.MULTI_SELECT_CATEGORY.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {

                    val gsonStringCategoryIds =
                        navController.previousBackStackEntry?.savedStateHandle?.get<String>(Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST)

                    val categoryIds: List<Long> = gsonStringCategoryIds?.let {
                        Gson().fromJson(it, object : TypeToken<List<Long>>() {}.type)
                    } ?: emptyList()

                    DialogMultiSelectCategory(
                        onNavigationUp = { navController.navigateUp() },
                        onSave = { selectedIds ->
                            // Pass data back to A

                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(
                                    Util.SAVE_STATE_SELECT_CATEGORY_ID_LIST,
                                    Gson().toJson(selectedIds)
                                )

                            navController.popBackStack()
                        },
                        selectedIds = categoryIds
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
        // PennyPalApp(preferenceRepository)
    }
}