package com.indie.apps.pennypal.data.module

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MoreItem(
    @DrawableRes val icon: Int? = null,
    @StringRes val title: Int,
    val subTitle: String? = null
)