package com.indie.apps.pannypal.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.indie.apps.pannypal.R
import com.indie.apps.pannypal.data.entity.toMerchantNameAndDetails
import com.indie.apps.pannypal.presentation.ui.common.Util
import com.indie.apps.pannypal.presentation.ui.dialog.DialogAddMerchant
import com.indie.apps.pannypal.presentation.ui.dialog.DialogAddPayment
import com.indie.apps.pannypal.presentation.ui.dialog.DialogCpp
import com.indie.apps.pannypal.presentation.ui.dialog.DialogSearchMerchant
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavigationBarCustom
import com.indie.apps.pannypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pannypal.presentation.ui.route.MerchantRoute
import com.indie.apps.pannypal.presentation.ui.route.OverViewRoute
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun PannyPalApp() {
    val context = LocalContext.current
    val paymentSaveToast = stringResource(id = R.string.payment_save_success_toast)
    val merchantSaveToast = stringResource(id = R.string.merchant_save_success_toast)
    PannyPalTheme() {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = BottomNavItem.values().find {
            currentDestination?.route?.startsWith(it.route + "/")
                ?: false
        }
            ?: BottomNavItem.OVERVIEW

        // State of bottomBar, set state to false, if current page route is "car_details"
        var bottomBarState = rememberSaveable { (mutableStateOf(true)) }

        Scaffold(
            bottomBar = {
                BottomNavigationBarCustom(
                    tabs = BottomNavItem.values(), onTabSelected = { newScreen ->
                        navController.navigate(newScreen.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(newScreen.route)
                        }
                    },
                    currentTab = currentScreen,
                    bottomBarState = bottomBarState.value
                )
            }) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = BottomNavItem.OVERVIEW.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                OverViewRoute(navController, bottomBarState)
                MerchantRoute(navController, bottomBarState)

                dialog(
                    route = DialogNav.SELECT_MERCHANT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogSearchMerchant(
                        onNavigationUp = {
                            navController.navigateUp()
                        },
                        onAddClick = {
                            navController.navigate(DialogNav.ADD_MERCHANT.route) {
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
                    route = DialogNav.ADD_MERCHANT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) { backStackEntry ->
                    // get data passed back from B
                    val data: String? = backStackEntry
                        .savedStateHandle
                        .get<String>(Util.SAVE_STATE_COUNTRY_CODE)

                    DialogAddMerchant(
                        onNavigationUp = { navController.navigateUp() },
                        onSaveSuccess = {
                            //navController.navigateUp()
                            Toast.makeText(context, merchantSaveToast, Toast.LENGTH_SHORT).show()

                            if (it != null)
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(
                                        Util.SAVE_STATE_MERCHANT_NAME_DESC,
                                        Gson().toJson(it.toMerchantNameAndDetails())
                                    )

                            navController.popBackStack()
                        },
                        onCpp = {
                            navController.navigate(DialogNav.CPP.route)
                        },
                        code = data
                    )
                }
                dialog(
                    route = DialogNav.EDIT_MERCHANT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                }
                dialog(
                    route = DialogNav.ADD_PAYMENT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogAddPayment(
                        onNavigationUp = { navController.navigateUp() },
                        onSaveSuccess = {
                            //navController.navigateUp()
                            Toast.makeText(context, paymentSaveToast, Toast.LENGTH_SHORT).show()

                            if (it != null)
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(Util.SAVE_STATE_PAYMENT, Gson().toJson(it))

                            navController.popBackStack()
                        }
                    )
                }
                dialog(
                    route = DialogNav.CPP.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogCpp(
                        onNavigationUp = { navController.navigateUp() },
                        onSelect = {
                            // Pass data back to A
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(Util.SAVE_STATE_COUNTRY_CODE, it.dial_code)

                            navController.popBackStack()
                        }
                    )
                }
            }

        }
    }

}

@Preview()
@Composable
private fun MainscreenPreview() {
    PannyPalTheme {
        PannyPalApp()
    }
}