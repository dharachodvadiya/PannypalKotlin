package com.indie.apps.pennypal.data.module

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.indie.apps.pennypal.util.SettingOption

data class MoreItem(
    @StringRes val title: Int,
    val option: SettingOption,
    @DrawableRes val icon: Int? = null,
    val subTitle: String? = null,
    val id: String = "",

)