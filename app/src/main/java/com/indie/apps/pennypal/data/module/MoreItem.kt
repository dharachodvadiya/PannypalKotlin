package com.indie.apps.pennypal.data.module

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.indie.apps.pennypal.util.SettingOption

data class MoreItem(
    @DrawableRes val icon: Int? = null,
    @StringRes val title: Int,
    val subTitle: String? = null,
    val id: String = "",
    val option: SettingOption
)