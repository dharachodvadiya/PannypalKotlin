package com.indie.apps.pennypal.data.module

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.indie.apps.pennypal.presentation.ui.component.UiText
import com.indie.apps.pennypal.util.app_enum.SettingOption

data class MoreItem(
    @StringRes val title: Int,
    val option: SettingOption,
    @DrawableRes val icon: Int? = null,
    val subTitle: UiText? = null,
    val id: String = "",

    )