package com.indie.apps.pennypal.presentation.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.util.Util

enum class DialogNav(val route: String) {
    SELECT_MERCHANT("Dialog/SelectMerchant"),
    ADD_EDIT_MERCHANT("Dialog/AddMerchant/{${Util.PARAM_MERCHANT_ID}}"),
    ADD_EDIT_PAYMENT("Dialog/AddEditPayment/{${Util.PARAM_PAYMENT_ID}}"),
    ADD_EDIT_CATEGORY("Dialog/AddEditCategory/{${Util.PARAM_CATEGORY_ID}}"),
    COUNTRY_PICKER("Dialog/CountryCodePicker"),
    CONTACT_PICKER("Dialog/ContactPicker"),
    SELECT_PAYMENT("Dialog/SelectPayment"),
    SELECT_CATEGORY("Dialog/SelectCategory"),
    MULTI_SELECT_CATEGORY("Dialog/MultiSelectCategory"),
    SELECT_BALANCE_VIEW("Dialog/SelectBalanceView"),
    SELECT_LANGUAGE("Dialog/SelectLanguage"),
}

enum class ScreenNav(val route: String) {
    BOARDING("boarding"),
    OVERVIEW_START("overview/start"),
    SEE_ALL_DATA("overview/see_all_data"),

    //NEW_ITEM("overview/new_item"),
    PROFILE("overview/profile"),
    CATEGORY_START("category/start"),
    MERCHANT_START("merchant/start"),
    MERCHANT_DATA("merchant/merchant_data/{${Util.PARAM_MERCHANT_ID}}"),
    MERCHANT_PROFILE("merchant/merchant_profile/{${Util.PARAM_MERCHANT_ID}}"),
    ADD_EDIT_MERCHANT_DATA("merchant/edit_merchant_data/{${Util.PARAM_EDIT_MERCHANT_DATA_ID}}"),
    PAYMENT_START("accounts/start"),
    SETTING_START("setting/start"),
    OVERVIEW_ANALYSIS("overview/analysis"),
    BUDGET("budget"),
    BUDGET_FILTER("budget_filter"),
    SINGLE_BUDGET_ANALYSIS("budget/{${Util.PARAM_BUDGET_ID}}"),

    //ADD_BUDGET("add_budget"),
    ADD_EDIT_BUDGET("add_edit_budget_data/{${Util.PARAM_BUDGET_ID}}"),
}

enum class BottomNavItem(
    @StringRes val title: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
    val route: String
) {
    OVERVIEW(R.string.overview, R.drawable.ic_overview_fill, R.drawable.ic_overview, "overview"),
    CATEGORY(R.string.category, R.drawable.ic_category_fill, R.drawable.ic_category, "category"),
    MERCHANTS(R.string.merchants, R.drawable.ic_person_fill, R.drawable.ic_person, "merchant"),
    ACCOUNTS(R.string.accounts, R.drawable.ic_bank_fill, R.drawable.ic_bank, "accounts"),
    SETTING(R.string.setting, R.drawable.ic_setting_fill, R.drawable.ic_setting, "setting"),
}

enum class OnBoardingPage {
    BEGIN,
    INTRO,
    SET_LANGUAGE,
    SET_NAME,
    SET_CURRENCY,
    WELCOME,
    RESTORE
}

