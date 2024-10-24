package com.indie.apps.pennypal.data.module

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItemInfo(
    val icon: ImageVector? = null,
    @StringRes val title: Int,
    val selectBgColor : Color,
    val unSelectBgColor : Color,
    val selectContentColor : Color,
    val unSelectContentColor : Color,
)