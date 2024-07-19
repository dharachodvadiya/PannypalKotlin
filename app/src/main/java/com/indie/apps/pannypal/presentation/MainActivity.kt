package com.indie.apps.pannypal.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavigationBarCustom
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavigationBarCustom1
import com.indie.apps.pannypal.presentation.ui.route.MerchantRoute
import com.indie.apps.pannypal.presentation.ui.route.OverViewRoute
import com.indie.apps.pannypal.presentation.ui.theme.PannyPalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PannyPalApp()
        }
    }
}

@Composable
fun PannyPalApp() {

    PannyPalTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            BottomNavItem.values().find { it.route == currentDestination?.route } ?: BottomNavItem.OVERVIEW

        Scaffold(
            bottomBar = {
                BottomNavigationBarCustom(
                    tabs = BottomNavItem.values(),
                    onTabSelected = { newScreen ->
                        navController.navigate(newScreen.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(newScreen.route)
                        }
                    },
                    currentTab = currentScreen
                )
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = BottomNavItem.OVERVIEW.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = BottomNavItem.OVERVIEW.route) {
                    OverViewRoute()
                }
                composable(route = BottomNavItem.MERCHANTS.route) {
                    MerchantRoute()
                }
            }

        }
    }
}


@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MainscreenPreviewDarkMode() {
    PannyPalTheme {
        PannyPalApp()
    }
}

@Preview()
@Composable
private fun MainscreenPreview() {
    PannyPalTheme {
        PannyPalApp()
    }
}

