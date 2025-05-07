package com.indie.apps.pennypal.util.app_enum


sealed class SettingEffect {
    data class OnCurrencyChange(val countryCode: String) : SettingEffect()
    data class OnDefaultPaymentChange(val id: Long) : SettingEffect()
    data object OnBalanceViewChange : SettingEffect()
    data object OnLanguageChange : SettingEffect()
    data object Share : SettingEffect()
    data object Rate : SettingEffect()
    data object PrivacyPolicy : SettingEffect()
    data object ContactUs : SettingEffect()
    data object Backup : SettingEffect()
    data object Restore : SettingEffect()
    data object GoogleSignIn : SettingEffect()
    data object GoogleSignInOrChange : SettingEffect()
    data object OnTransactions : SettingEffect()
    data object OnMerchants : SettingEffect()
    data object OnCategories : SettingEffect()
    data object OnBudgets : SettingEffect()

}

enum class SettingOption {
    CURRENCY_CHANGE,
    PAYMENT_CHANGE,
    BALANCE_VIEW_CHANGE,
    LANGUAGE_CHANGE,
    SHARE,
    RATE,
    PRIVACY_POLICY,
    CONTACT_US,
    BACKUP,
    RESTORE,
    GOOGLE_SIGN_IN,
    GOOGLE_SIGN_IN_OR_CHANGE,
    TRANSACTION,
    MERCHANT,
    CATEGORY,
    BUDGET
}