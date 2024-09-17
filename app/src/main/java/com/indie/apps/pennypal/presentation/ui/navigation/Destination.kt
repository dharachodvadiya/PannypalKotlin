package com.indie.apps.pennypal.presentation.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.util.Util

enum class DialogNav(val route: String) {
    SELECT_MERCHANT("Dialog/SelectMerchant"),
    ADD_EDIT_MERCHANT("Dialog/AddMerchant"),
    ADD_EDIT_PAYMENT("Dialog/AddEditPayment"),
    COUNTRY_PICKER("Dialog/CountryCodePicker"),
    CONTACT_PICKER("Dialog/ContactPicker"),
    SELECT_PAYMENT("Dialog/SelectPayment"),
    SELECT_CATEGORY("Dialog/SelectCategory"),
}

enum class ScreenNav(val route: String) {
    OVERVIEW_START("overview/start"),
    NEW_ITEM("overview/new_item"),
    PROFILE("overview/profile"),
    MERCHANT_START("merchant/start"),
    MERCHANT_DATA("merchant/merchant_data/{${Util.PARAM_MERCHANT_ID}}"),
    MERCHANT_PROFILE("merchant/merchant_profile/{${Util.PARAM_MERCHANT_ID}}"),
    EDIT_MERCHANT_DATA("merchant/edit_merchant_data/{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}"),
    PAYMENT_START("accounts/start"),
    SETTING_START("setting/start")
}

enum class BottomNavItem(
    @StringRes val title: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
    val route: String
) {
    OVERVIEW(R.string.overview, R.drawable.ic_overview_fill, R.drawable.ic_overview, "overview"),
    MERCHANTS(R.string.merchants, R.drawable.ic_person_fill, R.drawable.ic_person, "merchant"),
    ACCOUNTS(R.string.accounts, R.drawable.ic_bank_fill, R.drawable.ic_bank, "accounts"),
    SETTING(R.string.setting, R.drawable.ic_setting_fill, R.drawable.ic_setting, "setting"),
}

