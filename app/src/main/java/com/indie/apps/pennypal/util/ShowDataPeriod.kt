package com.indie.apps.pennypal.util

import com.indie.apps.pennypal.R

enum class ShowDataPeriod(
    val title: Int,
    val index: Int
) {

    THIS_MONTH(R.string.this_month, 1),
    THIS_YEAR(R.string.this_year, 3);

    companion object {
        fun fromIndex(index: Int): ShowDataPeriod? {
            return entries.find { it.index == index }
        }
    }
}
