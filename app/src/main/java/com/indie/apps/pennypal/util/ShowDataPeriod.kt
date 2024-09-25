package com.indie.apps.pennypal.util

enum class ShowDataPeriod(
    val title: String,
    val index: Int
) {

    THIS_MONTH("This Month", 1),
    THIS_YEAR("This Year", 3);

    companion object {
        fun fromIndex(index: Int): ShowDataPeriod? {
            return entries.find { it.index == index }
        }
    }
}
