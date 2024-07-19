package com.indie.apps.pannypal.presentation.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.indie.apps.pannypal.R

/*
sealed class BottomNavItem(val route: String, val icon: ImageVector, @StringRes val label: Int) {
    data object OverView : BottomNavItem("overview", Icons.Outlined.PieChart, R.string.overview)
    data object Merchants : BottomNavItem("merchants", Icons.Outlined.PersonOutline, R.string.merchants)
}*/

enum class BottomNavItem(
    @StringRes val title: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String
) {
    OVERVIEW(R.string.overview, Icons.Filled.PieChart,Icons.Outlined.PieChart, "overview"),
    MERCHANTS(R.string.merchants, Icons.Filled.Person,Icons.Outlined.PersonOutline, "merchants"),
}