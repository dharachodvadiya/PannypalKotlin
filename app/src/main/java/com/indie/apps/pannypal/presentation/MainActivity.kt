package com.indie.apps.pannypal.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavigationBar
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
                BottomNavigationBar(
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

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center)
                    {
                        Text(text = "OverView Screen")
                    }

                }
                composable(route = BottomNavItem.MERCHANTS.route) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center)
                    {
                        Text(text = "Merchant Screen")
                    }
                }
            }

        }
    }
}

