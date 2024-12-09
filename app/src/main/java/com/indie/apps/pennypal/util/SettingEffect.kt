package com.indie.apps.pennypal.util


sealed class SettingEffect {
    data class OnCurrencyChange(val countryCode: String) : SettingEffect()
    data class OnDefaultPaymentChange(val id: Long) : SettingEffect()
    data object OnBalanceViewChange : SettingEffect()
    data object Share : SettingEffect()
    data object Rate : SettingEffect()
    data object PrivacyPolicy : SettingEffect()
    data object ContactUs : SettingEffect()
    data object Backup : SettingEffect()
    data object Restore : SettingEffect()
    data object GoogleSignIn : SettingEffect()

}

enum class SettingOption {
    CURRENCY_CHANGE,
    PAYMENT_CHANGE,
    BALANCE_VIEW_CHANGE,
    SHARE,
    RATE,
    PRIVACY_POLICY,
    CONTACT_US,
    BACKUP,
    RESTORE,
    GOOGLE_SIGN_IN,
}