package com.indie.apps.pennypal.util.enum

import com.indie.apps.pennypal.util.ShowDataPeriod

enum class AnalysisPeriod(val id: Int) {
    MONTH(1),
    YEAR(2),
}

fun AnalysisPeriod.toShowDataPeriod(): ShowDataPeriod {
    return when (this) {
        AnalysisPeriod.MONTH -> ShowDataPeriod.THIS_MONTH
        AnalysisPeriod.YEAR -> ShowDataPeriod.THIS_YEAR
    }
}