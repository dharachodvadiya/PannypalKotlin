package com.indie.apps.pannypal.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.indie.apps.pannypal.presentation.ui.dialog.DialogAddContact
import com.indie.apps.pannypal.presentation.ui.dialog.DialogAddPayment
import com.indie.apps.pannypal.presentation.ui.dialog.DialogSearchContact
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavigationBarCustom
import com.indie.apps.pannypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pannypal.presentation.ui.route.MerchantRoute
import com.indie.apps.pannypal.presentation.ui.route.OverViewRoute
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme

@Composable
fun PannyPalApp() {

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

        Scaffold(bottomBar = {
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
                    route = DialogNav.SELECT_CONTACT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogSearchContact(
                        onNavigationUp = {navController.navigateUp()},
                        onAddClick = {
                            navController.navigate(DialogNav.ADD_CONTACT.route){
                                navController.popBackStack()
                            }
                        }
                    )
                }
                dialog(route = DialogNav.ADD_CONTACT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogAddContact(
                        onNavigationUp = {navController.navigateUp()}
                    )
                }
                dialog(route = DialogNav.EDIT_CONTACT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                }
                dialog(route = DialogNav.ADD_PAYMENT.route,
                    dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    DialogAddPayment(
                        onNavigationUp = {navController.navigateUp()}
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