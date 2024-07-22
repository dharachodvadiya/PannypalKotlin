package com.indie.apps.pannypal.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavItem
import com.indie.apps.pannypal.presentation.ui.navigation.BottomNavigationBarCustom
import com.indie.apps.pannypal.presentation.ui.navigation.DialogNav
import com.indie.apps.pannypal.presentation.ui.navigation.OverviewNav
import com.indie.apps.pannypal.presentation.ui.route.MerchantRoute
import com.indie.apps.pannypal.presentation.ui.route.OverViewRoute
import com.indie.apps.pannypal.presentation.ui.screen.NewItemScreen
import com.indie.apps.pannypal.presentation.ui.screen.OverViewStartScreen
import com.indie.apps.pannypal.presentation.ui.screen.ProfileScreen
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



