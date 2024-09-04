package com.indie.apps.pennypal.presentation.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.util.Util

enum class DialogNav(val route: String) {
    SELECT_MERCHANT("Dialog/SelectMerchant"),
    ADD_EDIT_MERCHANT("Dialog/AddMerchant"),
    ADD_PAYMENT("Dialog/AddPayment"),
    COUNTRY_PICKER("Dialog/CountryCodePicker"),
    CONTACT_PICKER("Dialog/ContactPicker")
}

enum class ScreenNav(val route: String) {
    OVERVIEW_START("overview/start"),
    NEW_ITEM("overview/new_item"),
    PROFILE("overview/profile"),
    MERCHANT_START("merchant/start"),
    MERCHANT_DATA("merchant/merchant_data/{${Util.PARAM_MERCHANT_ID}}"),
    MERCHANT_PROFILE("merchant/merchant_profile/{${Util.PARAM_MERCHANT_ID}}"),
    EDIT_MERCHANT_DATA("merchant/edit_merchant_data/{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}")
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

