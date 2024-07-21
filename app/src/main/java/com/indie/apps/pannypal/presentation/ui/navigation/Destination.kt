package com.indie.apps.pannypal.presentation.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.indie.apps.pannypal.R

enum class OverviewNav(val route: String) {
    START("overview/start"),
    NEW_ITEM("overview/new_item"),
    PROFILE("overview/profile")
}

sealed class MerchantNav(val route: String) {
    data object START : MerchantNav("merchant/start")
    data object DATA : MerchantNav("merchant/merchant_data")
}

enum class BottomNavItem(
    @StringRes val title: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String
) {
    OVERVIEW(R.string.overview, Icons.Filled.PieChart, Icons.Outlined.PieChart, "overview"),
    MERCHANTS(R.string.merchants, Icons.Filled.Person, Icons.Outlined.PersonOutline, "merchant"),
}